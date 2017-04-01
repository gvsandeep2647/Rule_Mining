package mining.rule;

public class DataRef {
	/** An array of Strings which store the corresponding preference of the candidate given the index number*/
	public String attrRef[] = new String[16];
	/**
	 * The constructor of the class where we declare all the stands that a candidate hass taken  
	 */
	DataRef() {
		attrRef[0]  = "handicapped-infants";
		attrRef[1]  = "water-project-cost-sharing";
		attrRef[2]  = "adoption-of-the-budget-resolution";
		attrRef[3]  = "physician-fee-freeze";
		attrRef[4]  = "el-salvador-aid";
		attrRef[5]  = "religious-groups-in-schools";
		attrRef[6]  = "anti-satellite-test-ban";
		attrRef[7]  = "aid-to-nicaraguan-contras";
		attrRef[8]  = "mx-missile";
		attrRef[9]  = "immigration";
		attrRef[10] = "synfuels-corporation-cutback";
		attrRef[11] = "education-spending";
		attrRef[12] = "superfund-right-to-sue";
		attrRef[13] = "crime";
		attrRef[14] = "duty-free-exports";
		attrRef[15] = "export-administration-act-south-africa";
	}
}