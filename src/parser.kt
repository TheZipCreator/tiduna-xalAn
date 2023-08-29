package tiduna;

sealed class Tree() {
	class Compound(val descriptors: List<String>) : Tree();
	class Phrase(val children: List<Tree>, val sound: Char?) : Tree();
	object Period : Tree();
}

fun phoneticize(c: Char): Char = when(c) {
	'g' -> 'ɢ'
	'w' -> 'ɸ'
	'v' -> 'β'
	'L' -> 'ɬ'
	'y' -> 'j'
	'A' -> 'ɑ'
	'e' -> 'ə'
	'n' -> 'ɴ'
	else -> c
}

fun phoneticize(str: String): String = buildString {
	for(c in str) {
		append(phoneticize(c));
	}
}

class Parser(val text: String, val doPhoneticization: Boolean = false) {
	var pos = 0;

	fun parse(sound: Char? = null): Tree {
		val ret = mutableListOf<Tree>();
		var curr = mutableListOf<String>();
		fun add() {
			if(curr.size == 0)
				return;
			if(doPhoneticization) {
				for(i in curr.indices)
					curr[i] = phoneticize(curr[i]);
			}
			ret.add(Tree.Compound(curr));
			curr = mutableListOf<String>();
		}
		while(true) {
			if(pos >= text.length) {
				add();
				return Tree.Phrase(ret, sound);
			}
			val c = text[pos];
			val next = if(pos+1 >= text.length) '\u0000' else text[pos+1];
			when(c) {
				',' -> {
					curr.add("");
				}
				' ', '\t', '\r', '\n' -> {
					add();
				}
				']' -> {
					add();
					return Tree.Phrase(ret, sound);
				}
				'.' -> {
					add();
					ret.add(Tree.Period);
				}
				else -> run {
					if(next == '[') {
						add();
						pos += 2;
						ret.add(parse(c));
						return@run;
					}
					if(curr.size == 0)
						curr.add(""+c);
					else
						curr[curr.size-1] = curr[curr.size-1]+c;
				}
			}
			pos++;
		}
	}
}
