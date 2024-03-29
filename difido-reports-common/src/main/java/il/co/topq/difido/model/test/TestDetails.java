package il.co.topq.difido.model.test;

import il.co.topq.difido.model.Enums.ElementType;
import il.co.topq.difido.model.Enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "description", "timestamp", "duration", "parameters", "properties", "reportElements" })
public class TestDetails {

	/**
	 * Required for updating the statuses of all the start level elements if one
	 * of the contained elements status is not success.
	 */
	@JsonIgnore
	private List<ReportElement> levelElementsBuffer;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description = "";

	@JsonProperty("timestamp")
	private String timeStamp;

	@JsonProperty("duration")
	private long duration;

	@JsonProperty("parameters")
	private Map<String, String> parameters;

	@JsonProperty("properties")
	private Map<String, String> properties;

	@JsonProperty("reportElements")
	private List<ReportElement> reportElements;

	@JsonProperty("uid")
	private String uid;

	public TestDetails(String name, String uid) {
		this.name = name;
		this.uid = uid;
	}

	public TestDetails() {

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(name).append("\n");
		sb.append("Description: ").append(description).append("\n");
		sb.append("Timestamp: ").append(timeStamp).append("\n");
		sb.append("Duration: ").append(duration).append("\n");
		return sb.toString();
	}

	@JsonIgnore
	public void addReportElement(ReportElement element) {
		if (null == reportElements) {
			reportElements = new ArrayList<ReportElement>();
		}
		if (element.getStatus() == null) {
			element.setStatus(Status.success);
		}
		element.setParent(this);
		reportElements.add(element);
		updateLevelElementsBuffer(element);
		updateLevelElementsStatuses(element);
	}

	/**
	 * Update the status of all the level elements in the buffer according to
	 * the specified element;
	 * 
	 * @param element
	 */
	@JsonIgnore
	private void updateLevelElementsStatuses(final ReportElement element) {
		if (null == levelElementsBuffer) {
			// The levelElementsBuffer should have been initialized in the
			// updateLevelElementsBuffer method. If this never happened, that
			// means that we never started a level
			return;
		}
		if (element == null || element.getType() == null) {
			return;
		}
		if (element.getStatus() == Status.success) {
			// Nothing to do
			return;
		}
		for (ReportElement startElement : levelElementsBuffer) {
			if (element.getStatus().ordinal() > startElement.getStatus().ordinal()) {
				startElement.setStatus(element.getStatus());
			}
		}
	}

	/**
	 * Adds start level elements to the buffer or remove element if the
	 * specified element is stop element
	 * 
	 * @param element
	 */
	@JsonIgnore
	private void updateLevelElementsBuffer(final ReportElement element) {
		if (element == null || element.getType() == null) {
			return;
		}
		if (element.getType() == ElementType.startLevel) {
			if (null == levelElementsBuffer) {
				levelElementsBuffer = new ArrayList<>();
			}
			levelElementsBuffer.add(element);
		} else if (element.getType() == ElementType.stopLevel) {
			if (levelElementsBuffer == null || levelElementsBuffer.size() == 0) {
				// Never should happen
				return;
			}
			levelElementsBuffer.remove(levelElementsBuffer.size() - 1);
		}
	}

	@JsonIgnore
	public void addProperty(String key, String value) {
		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		properties.put(key, value);
	}

	@JsonIgnore
	public void addParameter(String key, String value) {
		if (parameters == null) {
			parameters = new HashMap<String, String>();
		}
		parameters.put(key, value);
	}

	// @Override
	// @JsonIgnore
	// public int hashCode() {
	// int hash = 1;
	// if (parameters != null) {
	// hash = hash * 17 + parameters.hashCode();
	// }
	// if (properties != null) {
	// hash = hash * 13 + properties.hashCode();
	// }
	// if (name != null) {
	// hash = hash * 31 + name.hashCode();
	// }
	// return hash;
	// }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public List<ReportElement> getReportElements() {
		return reportElements;
	}

	public void setReportElements(List<ReportElement> reportElements) {
		this.reportElements = reportElements;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
