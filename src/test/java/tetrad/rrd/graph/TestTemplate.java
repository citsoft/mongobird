/*******************************************************************************
 * "mongobird" is released under a dual license model designed to developers 
 * and commercial deployment.
 * 
 * For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
 * Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
 * and another distributors, or for using include changed issue
 * (modify / application), it must have to follow the Commercial License policy.
 * To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
 * (http://www.citsoft.net)
 *  *
 * If not using Commercial License (Academic research or personal research),
 * it might to be under AGPL policy. To check the contents of the AGPL terms,
 * please see "http://www.gnu.org/licenses/"
 ******************************************************************************/
package tetrad.rrd.graph;
import org.rrd4j.ConsolFun;  
import org.rrd4j.core.Util;  
import org.rrd4j.core.XmlTemplate;  
import org.rrd4j.graph.RrdGraphConstants;
import org.rrd4j.graph.RrdGraphDef;
import org.w3c.dom.Node;  
import org.xml.sax.InputSource;    
import java.awt.*;  
import java.io.File;  
import java.io.IOException;  

public class TestTemplate extends XmlTemplate implements RrdGraphConstants {
	static final Color BLIND_COLOR = new Color(0, 0, 0, 0);    

	private RrdGraphDef rrdGraphDef; 

	public TestTemplate(InputSource inputSource) throws IOException {          
		super(inputSource);      
	}  
	public TestTemplate(File xmlFile) throws IOException {          
		super(xmlFile);      
	} 
	public TestTemplate(String xmlString) throws IOException {         
		super(xmlString);      
	}

	public RrdGraphDef getRrdGraphDef() {          
		// basic check          
		if (!root.getTagName().equals("rrd_graph_def")) {             
			throw new IllegalArgumentException("XML definition must start with <rrd_graph_def>");          
		}          
		validateTagsOnlyOnce(root, new String[]{"filename", "span", "options", "datasources", "graph"});          
		rrdGraphDef = new RrdGraphDef();          
		// traverse all nodes          
		Node[] childNodes = getChildNodes(root);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("filename")) {                  
				resolveFilename(childNode);              
			}              
			// SPAN              
			else if (nodeName.equals("span")) {                  
				resolveSpan(childNode);              
			}              
			// OPTIONS              
			else if (nodeName.equals("options")) {                 
				resolveOptions(childNode);              
			}              
			// DATASOURCES              
			else if (nodeName.equals("datasources")) {                  
				resolveDatasources(childNode);              
			}              
			// GRAPH ELEMENTS              
			else if (nodeName.equals("graph")) {                  
				resolveGraphElements(childNode);              
			}          
		}         
		return rrdGraphDef;      
	} 
	
	private void resolveGraphElements(Node graphNode) {          
		validateTagsOnlyOnce(graphNode, new String[]{"area*", "line*", "stack*",                  
				"print*", "gprint*", "hrule*", "vrule*", "comment*"});          
		Node[] childNodes = getChildNodes(graphNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("area")) {                  
				resolveArea(childNode);              
			}              
			else if (nodeName.equals("line")) {                  
				resolveLine(childNode);              
			}              
			else if (nodeName.equals("stack")) {                  
				resolveStack(childNode);              
			}              
			else if (nodeName.equals("print")) {                  
				resolvePrint(childNode, false);              
			}              
			else if (nodeName.equals("gprint")) {                  
				resolvePrint(childNode, true);              
			}              
			else if (nodeName.equals("hrule")) {                  
				resolveHRule(childNode);              
			}              
			else if (nodeName.equals("vrule")) {                  
				resolveVRule(childNode);              
			}              
			else if (nodeName.equals("comment")) {                  
				rrdGraphDef.comment(getValue(childNode, false));              
			}          
		}      
	}    

	private void resolveVRule(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"time", "color", "legend"});          
		long timestamp = Long.MIN_VALUE;          
		Paint color = null;          
		String legend = null;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("time")) {                  
				timestamp = Util.getTimestamp(getValue(childNode));              
			}              
			else if (nodeName.equals("color")) {                  
				color = getValueAsColor(childNode);              
			}             
			else if (nodeName.equals("legend")) {                  
				legend = getValue(childNode);              
			}          
		}          
		if (timestamp != Long.MIN_VALUE && color != null) {              
			rrdGraphDef.vrule(timestamp, color, legend);          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete VRULE settings");          
		}      
	} 
	
	private void resolveHRule(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"value", "color", "legend"});          
		double value = Double.NaN;         
		Paint color = null;          
		String legend = null;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("value")) {                  
				value = getValueAsDouble(childNode);              
			}              
			else if (nodeName.equals("color")) {                  
				color = getValueAsColor(childNode);              
			}              
			else if (nodeName.equals("legend")) {                  
				legend = getValue(childNode);              
			}          
		}          
		if (!Double.isNaN(value) && color != null) {              
			rrdGraphDef.hrule(value, color, legend);          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete HRULE settings");          
		}      
	} 

	private void resolvePrint(Node parentNode, boolean isInGraph) {          
		validateTagsOnlyOnce(parentNode, new String[]{"datasource", "cf", "format"});          
		String datasource = null, format = null;          
		ConsolFun consolFun = null;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("datasource")) {                  
				datasource = getValue(childNode);              
			}              
			else if (nodeName.equals("cf")) {                  
				consolFun = ConsolFun.valueOf(getValue(childNode));              
			}              
			else if (nodeName.equals("format")) {                  
				format = getValue(childNode);              
			}          
		}          
		if (datasource != null && consolFun != null && format != null) {              
			if (isInGraph) {                  
				rrdGraphDef.gprint(datasource, consolFun, format);              
			}              
			else {                  
				rrdGraphDef.print(datasource, consolFun, format);              
			}          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete " + (isInGraph ? "GRPINT" : "PRINT") + " settings");          
		}      
	}
	
	private void resolveStack(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"datasource", "color", "legend"});          
		String datasource = null, legend = null;          
		Paint color = null;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("datasource")) {                  
				datasource = getValue(childNode);              
			}              
			else if (nodeName.equals("color")) {                  
				color = getValueAsColor(childNode);              
			}              
			else if (nodeName.equals("legend")) {                  
				legend = getValue(childNode);              
			}          
		}          
		if (datasource != null) {              
			if (color != null) {                  
				rrdGraphDef.stack(datasource, color, legend);              
			}              
			else {                  
				rrdGraphDef.stack(datasource, BLIND_COLOR, legend);              
			}          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete STACK settings");          
		}      
	} 
	
	private void resolveLine(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"datasource", "color", "legend", "width"});          
		String datasource = null, legend = null;          
		Paint color = null;          
		float width = 1.0F;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("datasource")) {                  
				datasource = getValue(childNode);              
			}              
			else if (nodeName.equals("color")) {                  
				color = getValueAsColor(childNode);              
			}             
			else if (nodeName.equals("legend")) {                  
				legend = getValue(childNode);              
			}              
			else if (nodeName.equals("width")) {                  
				width = (float) getValueAsDouble(childNode);              
			}          
		}          
		if (datasource != null) {              
			if (color != null) {                  
				rrdGraphDef.line(datasource, color, legend, width);              
			}              
			else {                  
				rrdGraphDef.line(datasource, BLIND_COLOR, legend, width);              
			}          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete LINE settings");          
		}      
	}
	
	private void resolveArea(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"datasource", "color", "legend"});          
		String datasource = null, legend = null;          
		Paint color = null;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("datasource")) {                  
				datasource = getValue(childNode);              
			}              
			else if (nodeName.equals("color")) {                  
				color = getValueAsColor(childNode);              
			}              
			else if (nodeName.equals("legend")) {                  
				legend = getValue(childNode);              
			}          
		}          
		if (datasource != null) {              
			if (color != null) {                  
				rrdGraphDef.area(datasource, color, legend);              
			}              
			else {                  
				rrdGraphDef.area(datasource, BLIND_COLOR, legend);              
			}          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete AREA settings");          
		}      
	}
	
	private void resolveDatasources(Node datasourcesNode) {          
		validateTagsOnlyOnce(datasourcesNode, new String[]{"def*", "cdef*", "sdef*"});          
		Node[] childNodes = getChildNodes(datasourcesNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("def")) {                  
				resolveDef(childNode);              
			}              
			else if (nodeName.equals("cdef")) {                  
				resolveCDef(childNode);              
			}              
			else if (nodeName.equals("sdef")) {                  
				resolveSDef(childNode);              
			}          
		}      
	}

	private void resolveSDef(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"name", "source", "cf"});          
		String name = null, source = null;          
		ConsolFun consolFun = null;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("name")) {                  
				name = getValue(childNode);              
			}              
			else if (nodeName.equals("source")) {                  
				source = getValue(childNode);              
			}             
			else if (nodeName.equals("cf")) {                  
				consolFun = ConsolFun.valueOf(getValue(childNode));              
			}          
		}          
		if (name != null && source != null && consolFun != null) {              
			rrdGraphDef.datasource(name, source, consolFun);          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete SDEF settings");          
		}      
	}
	
	private void resolveCDef(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"name", "rpn"});          
		String name = null, rpn = null;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("name")) {                  
				name = getValue(childNode);              
			}              
			else if (nodeName.equals("rpn")) {                  
				rpn = getValue(childNode);              
			}          
		}          
		if (name != null && rpn != null) {              
			rrdGraphDef.datasource(name, rpn);          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete CDEF settings");          
		}      
	}
	
	private void resolveDef(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"name", "rrd", "source", "cf", "backend"});          
		String name = null, rrd = null, source = null, backend = null;          
		ConsolFun consolFun = null;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("name")) {                  
				name = getValue(childNode);              
			}              
			else if (nodeName.equals("rrd")) {                  
				rrd = getValue(childNode);              
			}              
			else if (nodeName.equals("source")) {                  
				source = getValue(childNode);              
			}              
			else if (nodeName.equals("cf")) {                  
				consolFun = ConsolFun.valueOf(getValue(childNode));              
			}              
			else if (nodeName.equals("backend")) {                  
				backend = getValue(childNode);              
			}          
		}          
		if (name != null && rrd != null && source != null && consolFun != null) {              
			rrdGraphDef.datasource(name, rrd, source, consolFun, backend);          
		}         
		else {              
			throw new IllegalArgumentException("Incomplete DEF settings");          
		}      
	}

	private void resolveFilename(Node filenameNode) {          
		String filename = getValue(filenameNode);          
		rrdGraphDef.setFilename(filename);      
	}

	private void resolveSpan(Node spanNode) {          
		validateTagsOnlyOnce(spanNode, new String[]{"start", "end"});          
		String startStr = getChildValue(spanNode, "start");          
		String endStr = getChildValue(spanNode, "end");          
		long[] span = Util.getTimestamps(startStr, endStr);          
		rrdGraphDef.setStartTime(span[0]);          
		rrdGraphDef.setEndTime(span[1]);      
	}
	
	private void resolveOptions(Node rootOptionNode) {          
		validateTagsOnlyOnce(rootOptionNode, new String[]{                  
				"anti_aliasing", "use_pool", "time_grid", "value_grid", "alt_y_grid", "alt_y_mrtg",                  
				"no_minor_grid", "alt_autoscale", "alt_autoscale_max", "units_exponent", "units_length",                  
				"vertical_label", "width", "height", "interlaced", "image_info", "image_format",                  
				"image_quality", "background_image", "overlay_image", "unit", "lazy",                  
				"min_value", "max_value", "rigid", "base", "logarithmic", "colors",                  
				"no_legend", "only_graph", "force_rules_legend", "title", "step", "fonts",                  
				"first_day_of_week", "signature"          
		});          
		Node[] optionNodes = getChildNodes(rootOptionNode);          
		for (Node optionNode : optionNodes) {              
			String option = optionNode.getNodeName();              
			if (option.equals("use_pool")) {                  
				rrdGraphDef.setPoolUsed(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("anti_aliasing")) {                  
				rrdGraphDef.setAntiAliasing(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("time_grid")) {                  
				resolveTimeGrid(optionNode);              
			}              
			else if (option.equals("value_grid")) {                  
				resolveValueGrid(optionNode);              
			}              
			else if (option.equals("no_minor_grid")) {                  
				rrdGraphDef.setNoMinorGrid(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("alt_y_grid")) {                  
				rrdGraphDef.setAltYGrid(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("alt_y_mrtg")) {                  
				rrdGraphDef.setAltYMrtg(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("alt_autoscale")) {                  
				rrdGraphDef.setAltAutoscale(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("alt_autoscale_max")) {                  
				rrdGraphDef.setAltAutoscaleMax(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("units_exponent")) {                  
				rrdGraphDef.setUnitsExponent(getValueAsInt(optionNode));              
			}              
			else if (option.equals("units_length")) {                  
				rrdGraphDef.setUnitsLength(getValueAsInt(optionNode));              
			}              
			else if (option.equals("vertical_label")) {                  
				rrdGraphDef.setVerticalLabel(getValue(optionNode));              
			}              
			else if (option.equals("width")) {                  
				rrdGraphDef.setWidth(getValueAsInt(optionNode));              
			}              
			else if (option.equals("height")) {                  
				rrdGraphDef.setHeight(getValueAsInt(optionNode));              
			}              
			else if (option.equals("interlaced")) {                  
				rrdGraphDef.setInterlaced(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("image_info")) {                  
				rrdGraphDef.setImageInfo(getValue(optionNode));              
			}              
			else if (option.equals("image_format")) {                  
				rrdGraphDef.setImageFormat(getValue(optionNode));              
			}              
			else if (option.equals("image_quality")) {                  
				rrdGraphDef.setImageQuality((float) getValueAsDouble(optionNode));              
			}              
			else if (option.equals("background_image")) {                  
				rrdGraphDef.setBackgroundImage(getValue(optionNode));              
			}              
			else if (option.equals("overlay_image")) {                  
				rrdGraphDef.setOverlayImage(getValue(optionNode));              
			}              
			else if (option.equals("unit")) {                  
				rrdGraphDef.setUnit(getValue(optionNode));              
			}              
			else if (option.equals("lazy")) {                  
				rrdGraphDef.setLazy(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("min_value")) {                  
				rrdGraphDef.setMinValue(getValueAsDouble(optionNode));              
			}              
			else if (option.equals("max_value")) {                  
				rrdGraphDef.setMaxValue(getValueAsDouble(optionNode));              
			}              
			else if (option.equals("rigid")) {                  
				rrdGraphDef.setRigid(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("base")) {                  
				rrdGraphDef.setBase(getValueAsDouble(optionNode));              
			}              
			else if (option.equals("logarithmic")) {                  
				rrdGraphDef.setLogarithmic(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("colors")) {                  
				resolveColors(optionNode);              
			}              
			else if (option.equals("no_legend")) {                  
				rrdGraphDef.setNoLegend(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("only_graph")) {                  
				rrdGraphDef.setOnlyGraph(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("force_rules_legend")) {                  
				rrdGraphDef.setForceRulesLegend(getValueAsBoolean(optionNode));              
			}              
			else if (option.equals("title")) {                  
				rrdGraphDef.setTitle(getValue(optionNode));              
			}              
			else if (option.equals("step")) {                  
				rrdGraphDef.setStep(getValueAsLong(optionNode));              
			}              
			else if (option.equals("fonts")) {                  
				resolveFonts(optionNode);              
			}              
			else if (option.equals("first_day_of_week")) {                  
				int dayIndex = resolveFirstDayOfWeek(getValue(optionNode));                  
				rrdGraphDef.setFirstDayOfWeek(dayIndex);              
			}              
			else if (option.equals("signature")) {                  
				rrdGraphDef.setShowSignature(getValueAsBoolean(optionNode));             
			}          
		}      
	}

	private static int resolveFirstDayOfWeek(String firstDayOfWeek) {          
		if (firstDayOfWeek.equalsIgnoreCase("sunday")) {              
			return SUNDAY;          
		}
		else if (firstDayOfWeek.equalsIgnoreCase("monday")) {              
			return MONDAY;          
		}          
		else if (firstDayOfWeek.equalsIgnoreCase("tuesday")) {              
			return TUESDAY;          
		}          
		else if (firstDayOfWeek.equalsIgnoreCase("wednesday")) {              
			return WEDNESDAY;          
		}          
		else if (firstDayOfWeek.equalsIgnoreCase("thursday")) {              
			return THURSDAY;          
		}          
		else if (firstDayOfWeek.equalsIgnoreCase("friday")) {              
			return FRIDAY;          
		}          
		else if (firstDayOfWeek.equalsIgnoreCase("saturday")) {              
			return SATURDAY;          
		}          
		throw new IllegalArgumentException("Never heard for this day of week: " + firstDayOfWeek);      
	} 
	
	private void resolveFonts(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"small_font", "large_font"});          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("small_font")) {                  
				rrdGraphDef.setSmallFont(resolveFont(childNode));              
			}              
			else if (nodeName.equals("large_font")) {                  
				rrdGraphDef.setLargeFont(resolveFont(childNode));              
			}          
		}      
	}
	
	private Font resolveFont(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"name", "style", "size"});          
		String name = null, style = null;          
		int size = 0;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("name")) {                  
				name = getValue(childNode);              
			}              
			else if (nodeName.equals("style")) {                  
				style = getValue(childNode).toLowerCase();              
			}              
			else if (nodeName.equals("size")) {                  
				size = getValueAsInt(childNode);              
			}          
		}          
		if (name != null && style != null && size > 0) {              
			boolean isItalic = style.contains("italic"), isBold = style.contains("bold");              
			int fstyle = Font.PLAIN;              
			if (isItalic && isBold) {                  
				fstyle = Font.BOLD + Font.ITALIC;              
			}              
			else if (isItalic) {                  
				fstyle = Font.ITALIC;              
			}              
			else if (isBold) {                  
				fstyle = Font.BOLD;              
			}              
			return new Font(name, fstyle, size);          
		}          
		else {              
			throw new IllegalArgumentException("Incomplete font specification");          
		}      
	}

	private void resolveColors(Node parentNode) {          
		// validateTagsOnly modifies the String[] that gets passed in          
		// therefore we must pass in a copy of COLOR_NAMES          
		String[] copy = new String[COLOR_NAMES.length];          
		System.arraycopy(COLOR_NAMES, 0, copy, 0, COLOR_NAMES.length);          
		validateTagsOnlyOnce(parentNode, copy);           
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String colorName = childNode.getNodeName();              
			rrdGraphDef.setColor(colorName, getValueAsColor(childNode));          
		}      
	}
	
	private void resolveValueGrid(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{"show_grid", "grid_step", "label_factor"});          
		boolean showGrid = true;          
		double gridStep = Double.NaN;          
		int NOT_SET = Integer.MIN_VALUE, labelFactor = NOT_SET;          
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("show_grid")) {                  
				showGrid = getValueAsBoolean(childNode);              
			}              
			else if (nodeName.equals("grid_step")) {                  
				gridStep = getValueAsDouble(childNode);              
			}              
			else if (nodeName.equals("label_factor")) {                  
				labelFactor = getValueAsInt(childNode);              
			}          
		}          
		rrdGraphDef.setDrawYGrid(showGrid);          
		if (!Double.isNaN(gridStep) && labelFactor != NOT_SET) {              
			rrdGraphDef.setValueAxis(gridStep, labelFactor);          
		}          
		else if (!Double.isNaN(gridStep) || labelFactor != NOT_SET) {              
			throw new IllegalArgumentException("Incomplete value axis settings");          
		}      
	}
	
	private void resolveTimeGrid(Node parentNode) {          
		validateTagsOnlyOnce(parentNode, new String[]{                  
				"show_grid", "minor_grid_unit",                  
				"minor_grid_unit_count", "major_grid_unit",                  
				"major_grid_unit_count", "label_unit", "label_unit_count",                  
				"label_span", "label_format"          
		});          
		boolean showGrid = true;          
		final int NOT_SET = Integer.MIN_VALUE;          
		int minorGridUnit = NOT_SET, minorGridUnitCount = NOT_SET,                  
				majorGridUnit = NOT_SET, majorGridUnitCount = NOT_SET,                  
				labelUnit = NOT_SET, labelUnitCount = NOT_SET, labelSpan = NOT_SET;          
		String labelFormat = null;         
		Node[] childNodes = getChildNodes(parentNode);          
		for (Node childNode : childNodes) {              
			String nodeName = childNode.getNodeName();              
			if (nodeName.equals("show_grid")) {                  
				showGrid = getValueAsBoolean(childNode);              
			}              
			else if (nodeName.equals("minor_grid_unit")) {                  
				minorGridUnit = resolveTimeUnit(getValue(childNode));              
			}              
			else if (nodeName.equals("minor_grid_unit_count")) {                  
				minorGridUnitCount = getValueAsInt(childNode);              
			}              
			else if (nodeName.equals("major_grid_unit")) {                  
				majorGridUnit = resolveTimeUnit(getValue(childNode));              
			}              
			else if (nodeName.equals("major_grid_unit_count")) {                  
				majorGridUnitCount = getValueAsInt(childNode);              
			}              
			else if (nodeName.equals("label_unit")) {                  
				labelUnit = resolveTimeUnit(getValue(childNode));              
			}              
			else if (nodeName.equals("label_unit_count")) {                  
				labelUnitCount = getValueAsInt(childNode);              
			}              
			else if (nodeName.equals("label_span")) {                  
				labelSpan = getValueAsInt(childNode);              
			}              
			else if (nodeName.equals("label_format")) {                  
				labelFormat = getValue(childNode);              
			}          
		}          
		rrdGraphDef.setDrawXGrid(showGrid);          
		if (minorGridUnit != NOT_SET && minorGridUnitCount != NOT_SET &&                  
				majorGridUnit != NOT_SET && majorGridUnitCount != NOT_SET &&                  
				labelUnit != NOT_SET && labelUnitCount != NOT_SET && labelSpan != NOT_SET && labelFormat != null) {              
			rrdGraphDef.setTimeAxis(minorGridUnit, minorGridUnitCount, majorGridUnit, majorGridUnitCount,                      
					labelUnit, labelUnitCount, labelSpan, labelFormat);          
		}          
		else if (minorGridUnit != NOT_SET || minorGridUnitCount != NOT_SET ||                  
				majorGridUnit != NOT_SET || majorGridUnitCount != NOT_SET ||                  
				labelUnit != NOT_SET || labelUnitCount != NOT_SET || labelSpan != NOT_SET || labelFormat != null) {              
			throw new IllegalArgumentException("Incomplete time axis settings");          
		}      
	}
	
	private static int resolveTimeUnit(String unit) {          
		if (unit.equalsIgnoreCase("second")) {              
			return RrdGraphConstants.SECOND;          
		}          
		else if (unit.equalsIgnoreCase("minute")) {              
			return RrdGraphConstants.MINUTE;          
		}          
		else if (unit.equalsIgnoreCase("hour")) {              
			return RrdGraphConstants.HOUR;          
		}          
		else if (unit.equalsIgnoreCase("day")) {              
			return RrdGraphConstants.DAY;          
		}          
		else if (unit.equalsIgnoreCase("week")) {              
			return RrdGraphConstants.WEEK;          
		}          
		else if (unit.equalsIgnoreCase("month")) {              
			return RrdGraphConstants.MONTH;          
		}          
		else if (unit.equalsIgnoreCase("year")) {              
			return RrdGraphConstants.YEAR;          
		}          
		throw new IllegalArgumentException("Unknown time unit specified: " + unit);          
	}  
}


