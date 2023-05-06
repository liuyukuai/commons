/*
 *
 */
package com.cutefool.commons.flyway.dm;

import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.parser.*;
import org.flywaydb.core.internal.sqlscript.Delimiter;
import org.flywaydb.core.internal.sqlscript.ParsedSqlStatement;
import org.flywaydb.core.internal.util.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author 271007729@qq.com
 * @date 2022/8/1 3:47 PM
 */
public class DMParser extends Parser {
    private static final List<String> FUNCTIONS = Arrays.asList("CREATE_TABLES", "MODIFY_TABLE_COMMENTS", "MODIFY_COLUMNS_COMMENTS", "MODIFY_COLUMNS", "DROP_COLUMNS", "RENAME_COLUMNS", "CREATE_INDEX", "DROP_INDEX", "RENAME_INDEX", "DROP_VIEWS", "CREATE_VIEWS");

    private static final Delimiter PLSQL_DELIMITER = new Delimiter("/", true);

    private static final Pattern PLSQL_TYPE_BODY_REGEX = Pattern.compile("^CREATE(\\sOR\\sREPLACE)?(\\s(NON)?EDITIONABLE)?\\sTYPE\\sBODY\\s([^\\s]*\\s)?(IS|AS)");

    private static final Pattern PLSQL_PACKAGE_BODY_REGEX = Pattern.compile("^CREATE(\\sOR\\sREPLACE)?(\\s(NON)?EDITIONABLE)?\\sPACKAGE\\sBODY\\s([^\\s]*\\s)?(IS|AS)");
    private static final StatementType PLSQL_PACKAGE_BODY_STATEMENT = new StatementType();

    private static final Pattern PLSQL_PACKAGE_DEFINITION_REGEX = Pattern.compile("^CREATE(\\sOR\\sREPLACE)?(\\s(NON)?EDITIONABLE)?\\sPACKAGE\\s([^\\s]*\\s)?(AUTHID\\s[^\\s]*\\s)?(IS|AS)");

    private static final Pattern PLSQL_VIEW_REGEX = Pattern.compile("^CREATE(\\sOR\\sREPLACE)?(\\s(NON)?EDITIONABLE)?\\sVIEW\\s([^\\s]*\\s)?AS\\sWITH\\s(PROCEDURE|FUNCTION)");
    private static final StatementType PLSQL_VIEW_STATEMENT = new StatementType();

    private static final Pattern PLSQL_REGEX = Pattern.compile("^CREATE(\\sOR\\sREPLACE)?(\\s(NON)?EDITIONABLE)?\\s(FUNCTION|PROCEDURE|TYPE|TRIGGER)");
    private static final Pattern DECLARE_BEGIN_REGEX = Pattern.compile("^DECLARE|BEGIN|WITH");
    private static final StatementType PLSQL_STATEMENT = new StatementType();

    private static final Pattern JAVA_REGEX = Pattern.compile("^CREATE(\\sOR\\sREPLACE)?(\\sAND\\s(RESOLVE|COMPILE))?(\\sNOFORCE)?\\sJAVA\\s(SOURCE|RESOURCE|CLASS)");
    private static final StatementType PLSQL_JAVA_STATEMENT = new StatementType();

    protected DMParser(Configuration configuration, ParsingContext parsingContext, int peekDepth) {
        super(configuration, parsingContext, peekDepth);
    }

    private static Pattern toRegex(String... commands) {
        return Pattern.compile(toRegexPattern(commands));
    }

    private static String toRegexPattern(String... commands) {
        return "^(" + StringUtils.arrayToDelimitedString("|", commands) + ")";
    }

    @Override
    protected ParsedSqlStatement createStatement(PeekingReader reader, Recorder recorder, int statementPos, int statementLine, int statementCol, int nonCommentPartPos, int nonCommentPartLine, int nonCommentPartCol, StatementType statementType, boolean canExecuteInTransaction, Delimiter delimiter, String sql) throws IOException {

        if (PLSQL_VIEW_STATEMENT == statementType) {
            sql = sql.trim();
            // Strip extra semicolon to avoid issues with WITH statements containing PL/SQL
            if (sql.endsWith(";")) {
                sql = sql.substring(0, sql.length() - 1);
            }
        }
        // liuyk TODO (其他数据连接池的支持）
        // 特殊处理达梦数据设置
        DataSource dataSource = this.configuration.getDataSource();
        if (dataSource instanceof HikariDataSource) {
            for (String function : FUNCTIONS) {
                String schema = ((HikariDataSource) dataSource).getSchema();
                if (org.apache.commons.lang3.StringUtils.isNotBlank(schema)) {
                    sql = sql.replace(function, schema + "." + function);
                    break;
                }
            }
        }
        return super.createStatement(reader, recorder, statementPos, statementLine, statementCol, nonCommentPartPos, nonCommentPartLine, nonCommentPartCol, statementType, canExecuteInTransaction, delimiter, sql);
    }


    @Override
    protected StatementType detectStatementType(String simplifiedStatement, ParserContext context) {
        if (PLSQL_PACKAGE_BODY_REGEX.matcher(simplifiedStatement).matches()) {
            return PLSQL_PACKAGE_BODY_STATEMENT;
        }

        if (PLSQL_REGEX.matcher(simplifiedStatement).matches() || PLSQL_PACKAGE_DEFINITION_REGEX.matcher(simplifiedStatement).matches() || DECLARE_BEGIN_REGEX.matcher(simplifiedStatement).matches()) {
            return PLSQL_STATEMENT;
        }

        if (JAVA_REGEX.matcher(simplifiedStatement).matches()) {
            return PLSQL_JAVA_STATEMENT;
        }

        if (PLSQL_VIEW_REGEX.matcher(simplifiedStatement).matches()) {
            return PLSQL_VIEW_STATEMENT;
        }

        return super.detectStatementType(simplifiedStatement, context);
    }

    @Override
    protected boolean shouldDiscard(Token token, boolean nonCommentPartSeen) {
        return ("/".equals(token.getText()) && !nonCommentPartSeen) || super.shouldDiscard(token, nonCommentPartSeen);
    }

    @Override
    protected void adjustDelimiter(ParserContext context, StatementType statementType) {
        if (statementType == PLSQL_STATEMENT || statementType == PLSQL_VIEW_STATEMENT || statementType == PLSQL_JAVA_STATEMENT || statementType == PLSQL_PACKAGE_BODY_STATEMENT) {
            context.setDelimiter(PLSQL_DELIMITER);

        } else {
            context.setDelimiter(Delimiter.SEMICOLON);
        }
    }

    @Override
    protected boolean shouldAdjustBlockDepth(ParserContext context, List<Token> tokens, Token token) {
        // Package bodies can have an unbalanced BEGIN without END in the initialisation section.
        TokenType tokenType = token.getType();
        if (context.getStatementType() == PLSQL_PACKAGE_BODY_STATEMENT && (TokenType.EOF == tokenType || TokenType.DELIMITER == tokenType)) {
            return true;
        }

        // In Oracle, symbols { } affect the block depth in embedded Java code
        if (token.getType() == TokenType.SYMBOL && context.getStatementType() == PLSQL_JAVA_STATEMENT) {
            return true;
        }
        return super.shouldAdjustBlockDepth(context, tokens, token);
    }

    // These words increase the block depth - unless preceded by END (in which case the END will decrease the block depth)
    private static final List<String> CONTROL_FLOW_KEYWORDS = Arrays.asList("IF", "LOOP", "CASE");

    @Override
    protected void adjustBlockDepth(ParserContext context, List<Token> tokens, Token keyword, PeekingReader reader) throws IOException {
        String keywordText = keyword.getText();

        // In embedded Java code we judge the end of a class definition by the depth of braces.
        // We ignore normal SQL keywords as Java code can contain arbitrary identifiers.
        if (context.getStatementType() == PLSQL_JAVA_STATEMENT) {
            if ("{".equals(keywordText)) {
                context.increaseBlockDepth(keyword.getText());
            } else if ("}".equals(keywordText)) {
                context.decreaseBlockDepth();
            }
            return;
        }

        int parensDepth = keyword.getParensDepth();

        if ("BEGIN".equals(keywordText) || (CONTROL_FLOW_KEYWORDS.contains(keywordText) && !lastTokenIs(tokens, parensDepth, "END")) || ("TRIGGER".equals(keywordText) && lastTokenIs(tokens, parensDepth, "COMPOUND")) || doTokensMatchPattern(tokens, keyword, PLSQL_PACKAGE_BODY_REGEX) || doTokensMatchPattern(tokens, keyword, PLSQL_PACKAGE_DEFINITION_REGEX) || doTokensMatchPattern(tokens, keyword, PLSQL_TYPE_BODY_REGEX)) {
            context.increaseBlockDepth(keyword.getText());
        } else if ("END".equals(keywordText)) {
            context.decreaseBlockDepth();
        }

        // Package bodies can have an unbalanced BEGIN without END in the initialisation section. This allows us
        // to exit the package even though we are still at block depth 1 due to the BEGIN.
        TokenType tokenType = keyword.getType();
        if (context.getStatementType() == PLSQL_PACKAGE_BODY_STATEMENT && (TokenType.EOF == tokenType || TokenType.DELIMITER == tokenType) && context.getBlockDepth() == 1) {
            context.decreaseBlockDepth();
            return;
        }
    }

    @Override
    protected boolean isDelimiter(String peek, ParserContext context, int col, int colIgnoringWhitepace) {
        Delimiter delimiter = context.getDelimiter();

        // Only consider alone-on-line delimiters (such as "/" for PL/SQL) if
        // we're at the top level and it's the first character on the line
        if (delimiter.shouldBeAloneOnLine() && (context.getBlockDepth() > 0 || col > 1)) {
            return false;
        }

        return super.isDelimiter(peek, context, col, colIgnoringWhitepace);
    }

    @Override
    protected boolean isAlternativeStringLiteral(String peek) {
        if (peek.length() < 3) {
            return false;
        }
        // Oracle's quoted-literal syntax is introduced by q (case-insensitive) followed by a literal surrounded by
        // any of !!, [], {}, (), <> provided the selected pair do not appear in the literal string; the others may do.
        char firstChar = peek.charAt(0);
        return (firstChar == 'q' || firstChar == 'Q') && peek.charAt(1) == '\'';
    }

    @Override
    protected Token handleAlternativeStringLiteral(PeekingReader reader, ParserContext context, int pos, int line, int col) throws IOException {
        reader.swallow(2);
        String closeQuote = computeAlternativeCloseQuote((char) reader.read());
        reader.swallowUntilExcluding(closeQuote);
        reader.swallow(closeQuote.length());
        return new Token(TokenType.STRING, pos, line, col, null, null, context.getParensDepth());
    }

    private String computeAlternativeCloseQuote(char specialChar) {
        switch (specialChar) {
            case '!':
                return "!'";
            case '[':
                return "]'";
            case '(':
                return ")'";
            case '{':
                return "}'";
            case '<':
                return ">'";
            default:
                return specialChar + "'";
        }
    }
}
