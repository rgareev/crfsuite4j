/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
class NativeLibrary {

	private static boolean loaded = false;

	public synchronized static void load() {
		if (!loaded) {
			System.loadLibrary("crfsuite-jni");
			loaded = true;
		}
	}
}