package tools.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import tools.json.charIters.ReaderCharsIters;
import tools.json.charIters.StringCharsIters;
import tools.json.types.JSONArray;
import tools.json.types.JSONBoolean;
import tools.json.types.JSONDouble;
import tools.json.types.JSONInt;
import tools.json.types.JSONLong;
import tools.json.types.JSONObject;
import tools.json.types.JSONString;

/**
 * Parse a JSON String into an object
 */
public class JSONParser {

	/**
	  */
	public static JSONValue parse(String input) { return parse(new StringCharsIters(input)); }

	public static JSONValue parseFile(Reader reader) throws FileNotFoundException {
		return parse(new ReaderCharsIters(reader));
	}

	public static JSONValue parseFile(String fullPath) throws FileNotFoundException {
		return parseFile(new FileReader(new File(fullPath)));
	}

	public static JSONValue parse(CharactersIterator source) {
		Objects.requireNonNull(source);
		if (!source.hasMoreChars()) { return new JSONObject(); }
		int[] indexLineColumn = { 0, 0 };
		/**
		 * holds:
		 * <ol>
		 * <li>line count (starts at 0)</li>
		 * <li>column count (starts at 0)</li>
		 */
		jumpWhitespaces(source, indexLineColumn);
		try {
			return parseValue(source, indexLineColumn);
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(
					"Unexpected end of input at line " + indexLineColumn[0] + " column " + indexLineColumn[1]);
		}
	}

	protected static void checkAndConsumeRightToken(CharactersIterator source, int[] indexLineColumn, char c) {
		char currentChar;
		currentChar = source.currentCharAndMove();
		if (currentChar != c) {
			final int maxDelta = 75;
			int prevStepsPerformed = 0, forwardStepsPerformed;
			StringBuilder sb;
			source.previousCharAndStepBack();
			sb = new StringBuilder(256);
			sb.append("Wrong character '").append(currentChar).append("' at line ").append(indexLineColumn[0]);
			sb.append(", column ").append(indexLineColumn[1]).append(", expected: --").append(c)
					.append("--\n\tsurrounded  by (at max) " + maxDelta
							+ " chars before and after (the extracted text starts from the beginning of line):\n");

			source.previousCharAndStepBack();
			while (prevStepsPerformed < maxDelta && source.hasPreviousChars()) {
				source.previousCharAndStepBack();
				prevStepsPerformed++;
			}
			forwardStepsPerformed = -(1 + prevStepsPerformed);
			for (int stepsTotalExtraction = (maxDelta + 1 + prevStepsPerformed); (stepsTotalExtraction > 0
					&& source.hasMoreChars()); stepsTotalExtraction--) {
				sb.append(source.currentCharAndMove());
				forwardStepsPerformed++;
				if (forwardStepsPerformed == 0) {
					sb.append("--THIS-->>");
				} else if (forwardStepsPerformed == 1) { sb.append("<<--THIS--"); }
			}
			while (forwardStepsPerformed-- > 0) {
				source.previousCharAndStepBack();
			}
			throw new IllegalArgumentException(sb.toString());
		}
		/*
		 * else { source.previous(); // just check the character, do not consume it }
		 */
		indexLineColumn[1]++;
	}

	protected static boolean isWhitespace(char c) {
		return (((char) 8) <= c // backspace, horizontal tab, new line, vert. tab, new page,
				&& c <= ((char) 15)) // carriage return, shift out, shift in
				|| c == ' ' || c == '\n' || c == '\r' || c == '\t' //
				|| Character.isWhitespace(c);
	}

	protected static void jumpWhitespaces(CharactersIterator source, int[] indexLineColumn) {
		boolean canJump;
		char c;
		if (!source.hasMoreChars()) { return; }
		do {
			c = source.currentCharAndMove();
			if (c == '\n' || c == '\r' || c == 13) {
				indexLineColumn[0]++;
				indexLineColumn[1] = 0;
			}
			if (isWhitespace(c)) {
				indexLineColumn[1]++;
				canJump = source.hasMoreChars(); // "true &&" trimmed away
			} else {
				canJump = false;
				source.previousCharAndStepBack();
			}
		} while (canJump);
	}

	protected static String extractString(CharactersIterator source, int[] indexLineColumn) {
		char c;
		int start;
		String text;
		StringBuilder sb;
		if (!source.hasMoreChars()) { return ""; }
		c = '\0';
		sb = new StringBuilder();
		start = source.currentIndex();
		while (source.hasMoreChars() && (c = source.currentCharAndMove()) != '\"' && c != ':' && c != '}' && c != ']') {
			// && (!isWhitespace(c))
			sb.append(c);
		}
		text = sb.toString();
		sb = null;
		if (c == '\"') { source.previousCharAndStepBack(); }
		indexLineColumn[1] += source.currentIndex() - start;
		return text;
	}

	/**
	 * Parse the next value and delegates to the specific parser token. <br>
	 * It assumes that the "index" parameter points to a non-whitespace character.
	 */
	protected static JSONValue parseValue(CharactersIterator source, int[] indexLineColumn) {
		char c;
		JSONValue val;
		val = null;
		c = source.currentCharAndMove();
		indexLineColumn[1]++;
		if (c == 't' || c == 'T' || c == 'f' || c == 'F') {
			source.previousCharAndStepBack();
			val = parseBoolean(source, indexLineColumn);
			if (val != null) { return val; }
		}
		if (c == '{') {
			jumpWhitespaces(source, indexLineColumn);
			val = parseObj(source, indexLineColumn);
			checkAndConsumeRightToken(source, indexLineColumn, '}');
			jumpWhitespaces(source, indexLineColumn);
		} else if (c == '[') {
			jumpWhitespaces(source, indexLineColumn);
			val = parseArray(source, indexLineColumn);
			// try to see if the next token is ']'
			checkAndConsumeRightToken(source, indexLineColumn, ']');
			jumpWhitespaces(source, indexLineColumn);
		} else if (c == '\"') {
			source.previousCharAndStepBack(); // "parseString" requires to check the '\"' char
			val = parseString(source, indexLineColumn);
			// try to see if the next token is '}'
			// the checks on '\"' char is already covered by the parseString
//			checkAndConsumeRightToken(source, indexLineColumn, '\"');
			jumpWhitespaces(source, indexLineColumn);
		} else if (c == '+' || c == '-' || Character.isDigit(c) || c == '.') {
			source.previousCharAndStepBack(); // do not consume the digit
			val = parseNumber(source, indexLineColumn);
		}
		if (val != null) { return val; }

		// simple string
		val = parseString(source, indexLineColumn);

		System.out.println(val);
		return val;
	}

	protected static JSONValue parseBoolean(CharactersIterator source, int[] indexLineColumn) {
		char c = source.currentCharAndMove();
		if (c == 't' || c == 'T') {
			c = source.currentCharAndMove();
			if (c == 'r' || c == 'R') {
				c = source.currentCharAndMove();
				if (c == 'u' || c == 'U') {
					c = source.currentCharAndMove();
					if (c == 'e' || c == 'E') {
						indexLineColumn[1] += 4;
						return new JSONBoolean(true);
					} else {
						source.previousCharAndStepBack();
					}
				} else {
					source.previousCharAndStepBack();
				}
			} else {
				source.previousCharAndStepBack();
			}
		} else if (c == 'f' || c == 'F') {
			c = source.currentCharAndMove();
			if (c == 'a' || c == 'A') {
				c = source.currentCharAndMove();
				if (c == 'l' || c == 'L') {
					c = source.currentCharAndMove();
					if (c == 's' || c == 'S') {
						c = source.currentCharAndMove();
						if (c == 'e' || c == 'E') {
							indexLineColumn[1] += 5;
							return new JSONBoolean(false);
						} else {
							source.previousCharAndStepBack();
						}
					} else {
						source.previousCharAndStepBack();
					}
				} else {
					source.previousCharAndStepBack();
				}
			} else {
				source.previousCharAndStepBack();
			}
		} else {
			source.previousCharAndStepBack();
		}
		return null;
	}

	/**
	 * Note: requires, checks and consumes the '\"' char surrounding the string.
	 * 
	 * @param source
	 * @param indexLineColumn
	 * @return
	 */
	protected static JSONValue parseString(CharactersIterator source, int[] indexLineColumn) {
		JSONValue stringVal;
		checkAndConsumeRightToken(source, indexLineColumn, '\"');
		stringVal = new JSONString(extractString(source, indexLineColumn));
		checkAndConsumeRightToken(source, indexLineColumn, '\"');
		return stringVal;
	}

	protected static JSONValue parseNumber(CharactersIterator source, int[] indexLineColumn) {
		boolean hasExp, hasDot, canContinue, hasError;
		char c;
		int start, end;
		StringBuilder sb;
		start = source.currentIndex();
		end = start;
		sb = new StringBuilder();
		c = source.currentCharAndMove();
		if (c == '-' || c == '+') {
			end++;
			sb.append(c);
		} else {
			source.previousCharAndStepBack();
		}
		canContinue = true;
		hasError = hasExp = hasDot = false;
		do {
			c = source.currentCharAndMove();
			if (Character.isDigit(c)) {
				canContinue = true;
				end++;
				sb.append(c);
			} else if (c == '.') {
				canContinue = !hasDot;
				hasError = hasDot;
				hasDot = true;
				if (canContinue) {
					end++;
					sb.append(c);
				} else {
					source.previousCharAndStepBack();
				}
			} else if (c == 'e' || c == 'E') {
				canContinue = !hasExp;
				hasError = hasExp;
				hasExp = true;
				if (canContinue) {
					end++;
					sb.append(c);
					c = source.currentCharAndMove();
					if (c == '-' || c == '+') {
						end++;
						sb.append(c);
					} else {
						source.previousCharAndStepBack();
					}
				} else {
					source.previousCharAndStepBack();
				}
			} else {
				canContinue = false;
				source.previousCharAndStepBack();
				// check for next probable tokens
				if (c != ',' && c != ']' && c != '}' && (!isWhitespace(c))) { hasError = true; }
			}
		} while (canContinue && source.hasMoreChars());

		indexLineColumn[1] += end - start;
		if (hasError) {
			throw new IllegalArgumentException(
					"Wrong number format at line " + indexLineColumn[0] + ", column " + indexLineColumn[1]
							+ ":\n\t at start(" + start + "), end (" + end + ") we have text:" + sb.toString());
		}

		String numText = sb.toString();
		sb = null;
		if (hasDot) {
			try {
				double value = Double.parseDouble(numText);
				return new JSONDouble(value);
			} catch (Exception e2) {
				e2.printStackTrace();
				throw e2;
			}
		} else {
			try {
				long value = Long.parseLong(numText);
				if (value <= (Integer.MAX_VALUE) || value >= (Integer.MIN_VALUE)) {
					return new JSONInt((int) value);
				} else {
					return new JSONLong(value);
				}
			} catch (Exception e) {
				try {
					double value = Double.parseDouble(numText);
					return new JSONDouble(value);
				} catch (Exception e2) {
					e2.printStackTrace();
					throw e2;
				}
			}
		}
	}

	protected static JSONValue parseObj(CharactersIterator source, int[] indexLineColumn) {
		boolean canGo;
		char c;
		JSONObject obj;
		JSONValue fVal, field;
		obj = new JSONObject();
		c = source.currentCharAndMove();
		if (canGo = c != '}') {
			source.previousCharAndStepBack();
			do {
				jumpWhitespaces(source, indexLineColumn);
				field = parseString(source, indexLineColumn);
				jumpWhitespaces(source, indexLineColumn);
				try {
					checkAndConsumeRightToken(source, indexLineColumn, ':');
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(e.getMessage() + "\n\tDid you miss a double comma (\")?");
				}

				jumpWhitespaces(source, indexLineColumn);
				fVal = parseValue(source, indexLineColumn);
				obj.addField(field.asString(), fVal);

				jumpWhitespaces(source, indexLineColumn);
				c = source.currentCharAndMove();

				if (c == ',') {
					indexLineColumn[1]++;
					jumpWhitespaces(source, indexLineColumn);
					c = source.currentCharAndMove();
					source.previousCharAndStepBack();
					canGo = c != '}';
				} else {
					canGo = false;
					source.previousCharAndStepBack();
				}
			} while (canGo);
		} else {
			source.previousCharAndStepBack();
		}
		return obj;
	}

	protected static JSONValue parseArray(CharactersIterator source, int[] indexLineColumn) {
		boolean canContinue, arrayOk;
		char c;
		JSONValue e = null;
		JSONTypes firstElementType, arrayType;
		List<JSONValue> values;
		firstElementType = JSONTypes.Object;
		arrayType = JSONTypes.ArrayHomogeneousType;
		values = new LinkedList<>();
		c = source.currentCharAndMove();
		arrayOk = true; // hypotesis: empty array is always ok
		if (canContinue = c != ']') {
			source.previousCharAndStepBack();
			do {
				jumpWhitespaces(source, indexLineColumn);
				c = source.currentCharAndMove();
				source.previousCharAndStepBack();
				if (c != ']') {
					e = parseValue(source, indexLineColumn);
					if (e == null) {
						arrayOk = false;
					} else {
						if (values.isEmpty()) {
							firstElementType = e.getType();
						} else if (firstElementType != e.getType()) { arrayType = JSONTypes.ArrayMiscTypes; }
						values.add(e);

						jumpWhitespaces(source, indexLineColumn);
						c = source.currentCharAndMove();
						if (c == ',') {
							canContinue = true;
							indexLineColumn[1]++;
							jumpWhitespaces(source, indexLineColumn);
							c = source.currentCharAndMove();
						}

						if (c == ']') {
							canContinue = false;
							arrayOk = true;
						}
						source.previousCharAndStepBack();
					}
				} else {
					canContinue = false;
					arrayOk = true;
				}
			} while (canContinue);
		} else {
			source.previousCharAndStepBack();
		}
		if (arrayOk) {
			// creation of the array
			if (values.size() == 0) {
				e = new JSONArray(JSONTypes.ArrayHomogeneousType, new JSONValue[0], JSONTypes.Object);
			} else {
				e = new JSONArray(arrayType, values.toArray(new JSONValue[values.size()]), firstElementType);
			}
		}
		return e;
	}
}