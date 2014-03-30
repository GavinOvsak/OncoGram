package com.example.oncogram;

import java.util.Date;
 
public class Profile {
	private String myName;
	//expected birthday of baby
	private String myBabyBirth;
	private String myBirth;
	private String myLoc;
	private int myAge;
	private char myGen;
	
	public Profile(String name)
	{
		myName = name;
		myBabyBirth = " ";
		myBirth = " ";
		myLoc = " ";
		myAge = 0;
		myGen = ' ';
	}
	public Profile(String name, String babyBirth, String selfBirth,
			String loc, int age, char gen)
	{
		myName = name;
		myBabyBirth = babyBirth;
		myBirth = selfBirth;
		myLoc = loc;
		myAge = age;
		myGen = gen;
	}
	//getter methods for info
	public String getName()
	{
		return myName;
	}
	
	public String getBabyBirth()
	{
		return myBabyBirth;
	}
	
	public String getMyBirth()
	{
		return myBirth;
	}
	
	public String getLoc()
	{
		return myLoc;
	}
	
	public int getAge()
	{
		return myAge;
	}
	
	public char getGen()
	{
		return myGen;
	}
	
	//setter methods for each profile info
	public void setName(String nam)
	{
		myName = nam;
	}
	
	public void setBabyBirth(String dat)
	{
		myBabyBirth = dat;
	}
	
	public void setMyBirth(String dat)
	{
		myBirth = dat;
	}
	
	public void setLoc(String loc)
	{
		myLoc = loc;
	}
	
	public void setAge(int age)
	{
		myAge = age;
	}
	
	public void setGen(char gen)
	{
		myGen = gen;
	}
}
