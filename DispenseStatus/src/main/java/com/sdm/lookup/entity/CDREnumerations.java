package com.sdm.lookup.entity;

public class CDREnumerations {

	public enum Banner {
	    SDM,
	    LCL,
	    OTHERS;

	    public String value() {
	        return name();
	    }

	    public static Banner fromValue(String v) {
	        return valueOf(v);
	    }

	};
	
	
	
	public enum Province {

	    NOT_SPECIFIED("Not Specified"),
	    ALASKA("Alaska"),
	    ALABAMA("Alabama"),
	    ARKANSAS("Arkansas"),
	    ARIZONA("Arizona"),
	    CALIFORNIA("California"),
	    COLORADO("Colorado"),
	    CONNECTICUT("Connecticut"),
	    DISTRICT_OF_COLUMBIA("District Of Columbia"),
	    DELAWARE("Delaware"),
	    FLORIDA("Florida"),
	    GEORGIA("Georgia"),
	    HAWAII("Hawaii"),
	    IOWA("Iowa"),
	    IDAHO("Idaho"),
	    ILLINOIS("Illinois"),
	    INDIANA("Indiana"),
	    KANSAS("Kansas"),
	    KENTUCKY("Kentucky"),
	    LOUISIANA("Louisiana"),
	    MASSACHUSETTS("Massachusetts"),
	    MARYLAND("Maryland"),
	    MAINE("Maine"),
	    MICHIGAN("Michigan"),
	    MINNESOTA("Minnesota"),
	    MISSOURI("Missouri"),
	    MISSISSIPPI("Mississippi"),
	    MONTANA("Montana"),
	    NORTH_CAROLINA("North Carolina"),
	    NORTH_DAKOTA("North Dakota"),
	    NEBRASKA("Nebraska"),
	    VERMONT("Vermont"),
	    NEW_HAMPSHIRE("New Hampshire"),
	    NEW_JERSEY("New Jersey"),
	    NEW_MEXICO("New Mexico"),
	    NEVADA("Nevada"),
	    NEW_YORK("New York"),
        OHIO("Ohio"),
	    OKLAHOMA("Oklahoma"),
	    OREGON("Oregon"),
	    PENNSYLVANIA("Pennsylvania"),
	    RHODE_ISLAND("Rhode Island"),
	    SOUTH_CAROLINA("South Carolina"),
	    SOUTH_DAKOTA("South Dakota"),
	    TENNESSEE("Tennessee"),
	    TEXAS("Texas"),
	    UTAH("Utah"),
	    VIRGINIA("Virginia"),
	    WASHINGTON("Washington"),
	    WISCONSIN("Wisconsin"),
	    WEST_VIRGINIA("West Virginia"),
	    WYOMING("Wyoming"),
	    
	    ALBERTA("alberta"),
	    QUEBEC("quebec"),
	    ONTARIO("ontario"),
	    MANITOBA("manitoba"),
	    NEW_BRUNSWICK("newBrunswick"),
	    NEWFOUNDLAND_AND_LABRADOR("newfoundlandAndLabrador"),
	    NOVA_SCOTIA("novaScotia"),
	    NORTHWEST_TERRITORIES("northwestTerritories"),
	    NUNAVUT("nunavut"),
	    PRINCE_EDWARD_ISLAND("princeEdwardIsland"),
	    SASKATCHEWAN("saskatchewan"),
	    YUKON("yukon"),
	    BRITISH_COLUMBIA("britishColumbia");
		
	    private final String value;

	    Province(String v) {
	        value = v;
	    }

	    public String value() {
	        return value;
	    }

	    public static Province fromValue(String v) {
	        for (Province c: Province.values()) {
	            if (c.value.equals(v)) {
	                return c;
	            }
	        }
	        throw new IllegalArgumentException(v);
	    }
    }
	
}
