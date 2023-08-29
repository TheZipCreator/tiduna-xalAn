package tiduna;
// #!/usr/bin/env -S kotlinc -script

fun main(args: Array<String>) {
	if(args.size == 0) {
		println("Usage: phoneticize [phrases...]");
		return;
	}
	for(string in args) {
		val tree = Parser(string, true).parse();

		var maxLen = 0;
		var numPhrases = 0;

		fun findLen(t: Tree, depth: Int = 0) {
			when(t) {
				is Tree.Compound -> {
					if(t.descriptors.size > maxLen)
						maxLen = t.descriptors.size;
				}
				is Tree.Phrase -> {
					if(depth > numPhrases)
						numPhrases = depth;
					for(c in t.children)
						findLen(c, depth+1);
				}
				else -> {}
			}
		}

		findLen(tree);


		val words = MutableList(maxLen) { "" }
		val sounds = MutableList(numPhrases) { "" }

		fun ipa(t: Tree, layers: List<Char> = listOf()) {
			when(t) {
				is Tree.Compound -> {
					val len = (t.descriptors.maxOf { it.length })+1;
					for(i in words.indices) {
						val d = t.descriptors.elementAtOrNull(i);
						for(j in 0..<len) {
							if(d == null || j >= d.length)
								words[i] = words[i]+' ';
							else
								words[i] = words[i]+d[j];
						}
					}
					for(j in 0..<len) {
						for(i in sounds.indices) {
							if(i < layers.size)
								sounds[i] = sounds[i]+layers[i];
							else
								sounds[i] = sounds[i]+' ';
						}
					}
				}
				is Tree.Period -> {
					for(i in words.indices)
						words[i] = words[i]+". ";
					for(i in sounds.indices)
						sounds[i] = sounds[i]+". ";
				}
				is Tree.Phrase -> {
					val l = layers.toMutableList();
					if(t.sound != null)
						l.add(phoneticize(t.sound));
					for(c in t.children)
						ipa(c, l);
				}
			}
		}

		ipa(tree);

		fun join(l: List<String>) = buildString {
			for(s in l)
				append("/$s/\n");
		}

		println(join(words)+join(sounds));
	}
}
