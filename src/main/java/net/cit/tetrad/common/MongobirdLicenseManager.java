package net.cit.tetrad.common;

import java.util.HashMap;
import java.util.Map;

import net.cit.monad.Operations;
import net.cit.tetrad.utility.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Query;

public class MongobirdLicenseManager {
	private Logger logger = Logger.getLogger(this.getClass());
	private final String collname = "license";
	private Operations operations;

	public void setOperations(Operations operations) {
		this.operations = operations;
	}
	
	public int registLicensekey(String licensekey) {
		int isSuccess = ColumnConstent.REGIST_FAIL_INVALID;
		try {
			Map<String, String> licenseMap = new HashMap<String, String>();
			licenseMap.put("licensekey", licensekey);
			operations.insert(licenseMap, collname);
			isSuccess = ColumnConstent.REGIST_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	@SuppressWarnings("unchecked")
	public String getLicensekey() {
		String licensekey = "";
		try {
			Map<String, String> result = operations.findOne(new Query(), Map.class, collname);
			if (result != null) licensekey = result.get("licensekey");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return licensekey;
	}

	public String getLicenseType(String licensekey) {
		if (StringUtils.isNull(licensekey)) {
			return "";
		} else {
			String[] words = licensekey.split("-");

			return LicenseTypeEnum.fromTypeCode(words[4]).getTypeValue();
		}
	}

	public String convertLicensekey(String licensekey) {		
		return StringUtils.isNull(licensekey) ? "" : licensekey.substring(0, 10) + "****-****"; 
	}
}
