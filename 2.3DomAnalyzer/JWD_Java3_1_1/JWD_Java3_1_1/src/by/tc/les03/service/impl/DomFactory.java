package by.tc.les03.service.impl;

import by.tc.les03.service.DomParser;

public class DomFactory {
	private static final DomFactory instance = new DomFactory();

	private DomFactory(){}

	/**
	 *
	 * @return
     */
	public static DomFactory getInstance(){
		return instance;
	}

	/**
	 *
	 * @return
     */
	public DomParser create(){
		return new BaseDomParser();
	}

}
