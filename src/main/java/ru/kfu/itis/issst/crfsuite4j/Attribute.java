/**
 * 
 */
package ru.kfu.itis.issst.crfsuite4j;

/**
 * @author Rinat Gareev (Kazan Federal University)
 * 
 */
public class Attribute {

	private String name;
	private double weight;

	public Attribute(String name, double weight) {
		this.name = name;
		this.weight = weight;
	}

	public Attribute(String name) {
		this(name, 1);
	}

	public String getName() {
		return name;
	}

	public double getWeight() {
		return weight;
	}
}