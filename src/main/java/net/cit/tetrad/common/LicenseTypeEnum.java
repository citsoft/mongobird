package net.cit.tetrad.common;


public enum LicenseTypeEnum {	
	ACADEMY_DEVELOPER("TADV", "Public"),
	STANDARD("TSTD", "Basic"),
	BUSINESS("TBSN", "Professional"),
	ENTERPRISE("TETP", "Enterprise");
	
	private String typeCode;
	private String typeValue;
	
	private LicenseTypeEnum(String typeCode, String typeValue) {
		this.typeCode = typeCode;
		this.typeValue = typeValue;
	}

	public String getTypeValue() {
		return typeValue;
	}
	
	public static LicenseTypeEnum fromTypeCode( String typeCode ){
		LicenseTypeEnum result = null;
        final LicenseTypeEnum [] types = LicenseTypeEnum.values();
        for( LicenseTypeEnum type : types ){
            if( type.typeCode.equals(typeCode)){
                result = type;
                break;
            }
        }
        return result;
    }
}
