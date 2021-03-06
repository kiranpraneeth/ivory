package org.apache.ivory.replication;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.Credentials;
import org.apache.hadoop.tools.DistCpOptions;
import org.apache.hadoop.tools.SimpleCopyListing;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.regex.Pattern;

public class FilteredCopyListing extends SimpleCopyListing {
    private static final Logger LOG = Logger.getLogger(FilteredCopyListing.class);

    /** Default pattern character: Escape any special meaning. */
    private static final char  PAT_ESCAPE = '\\';
    /** Default pattern character: Any single character. */
    private static final char  PAT_ANY = '.';
    /** Default pattern character: Character set close. */
    private static final char  PAT_SET_CLOSE = ']';

    private Pattern regex;

    protected FilteredCopyListing(Configuration configuration, Credentials credentials) {
        super(configuration, credentials);
        try {
            regex = getRegEx(configuration.get("ivory.include.path", "").trim());
            LOG.info("Inclusion pattern = " + configuration.get("ivory.include.path"));
            LOG.info("Regex pattern = " + regex);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to build regex for " +
                    configuration.get("ivory.include.path", ""));
        }
    }

    @Override
    protected boolean shouldCopy(Path path, DistCpOptions options) {
        return regex == null || regex.matcher(path.toString()).find();
    }

    private boolean isJavaRegexSpecialChar(char pChar) {
        return pChar == '.' || pChar == '$' || pChar == '(' || pChar == ')' ||
                pChar == '|' || pChar == '+';
    }

    private Pattern getRegEx(String filePattern) throws IOException {
        int len;
        int setOpen;
        int curlyOpen;
        boolean setRange;

        StringBuilder fileRegex = new StringBuilder();

        // Validate the pattern
        len = filePattern.length();
        if (len == 0)
            return null;

        setOpen = 0;
        setRange = false;
        curlyOpen = 0;

        for (int i = 0; i < len; i++) {
            char pCh;

            // Examine a single pattern character
            pCh = filePattern.charAt(i);
            if (pCh == PAT_ESCAPE) {
                fileRegex.append(pCh);
                i++;
                if (i >= len)
                    error("An escaped character does not present", filePattern, i);
                pCh = filePattern.charAt(i);
            } else if (isJavaRegexSpecialChar(pCh)) {
                fileRegex.append(PAT_ESCAPE);
            } else if (pCh == '*') {
                fileRegex.append(PAT_ANY);
            } else if (pCh == '?') {
                pCh = PAT_ANY;
            } else if (pCh == '{') {
                fileRegex.append('(');
                pCh = '(';
                curlyOpen++;
            } else if (pCh == ',' && curlyOpen > 0) {
                fileRegex.append(")|");
                pCh = '(';
            } else if (pCh == '}' && curlyOpen > 0) {
                // End of a group
                curlyOpen--;
                fileRegex.append(")");
                pCh = ')';
            } else if (pCh == '[' && setOpen == 0) {
                setOpen++;
            } else if (pCh == '^' && setOpen > 0) {
            } else if (pCh == '-' && setOpen > 0) {
                // Character set range
                setRange = true;
            } else if (pCh == PAT_SET_CLOSE && setRange) {
                // Incomplete character set range
                error("Incomplete character set range", filePattern, i);
            } else if (pCh == PAT_SET_CLOSE && setOpen > 0) {
                // End of a character set
                if (setOpen < 2)
                    error("Unexpected end of set", filePattern, i);
                setOpen = 0;
            } else if (setOpen > 0) {
                // Normal character, or the end of a character set range
                setOpen++;
                setRange = false;
            }
            fileRegex.append(pCh);
        }

        // Check for a well-formed pattern
        if (setOpen > 0 || setRange || curlyOpen > 0) {
            // Incomplete character set or character range
            error("Expecting set closure character or end of range, or }",
                    filePattern, len);
        }
        return Pattern.compile("(" + fileRegex.toString() + "/)|(" + fileRegex.toString() + "$)");
    }

    private void error(String s, String pattern, int pos) throws IOException {
        throw new IOException("Illegal file pattern: "
                +s+ " for glob "+ pattern + " at " + pos);
    }

    @Override
    public long getBytesToCopy() {
        return super.getBytesToCopy();
    }

    @Override
    public long getNumberOfPaths() {
        return super.getNumberOfPaths();
    }
}
