package com.wpits.helpers;
class Animal{
	void print(Animal animal) {
		System.out.println("this from Animal");
	}
}
class Dog extends Animal{

	@Override
	void print(Animal animal) {
		System.out.println("this is from dog");
	}
	
}


public class Test {

	public static void main(String[] args) {
		
		Dog dog = new Dog();
		dog.print(dog);
		
		Animal ani_mal =new Dog();
		ani_mal.print(ani_mal);
		
		Animal animal = new Animal();
		animal.print(animal);
	}

}
