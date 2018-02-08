package org.D7noun.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;

@Entity
public class Connection implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column
	private String connectionName;

	@Column
	private String ipAddress;

	@Column
	private String macAddress;

	@Column
	private String portNumbers;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Connection)) {
			return false;
		}
		Connection other = (Connection) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getPortNumbers() {
		return portNumbers;
	}

	public void setPortNumbers(String portNumbers) {
		this.portNumbers = portNumbers;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (connectionName != null && !connectionName.trim().isEmpty())
			result += "connectionName: " + connectionName;
		if (ipAddress != null && !ipAddress.trim().isEmpty())
			result += ", ipAddress: " + ipAddress;
		if (macAddress != null && !macAddress.trim().isEmpty())
			result += ", macAddress: " + macAddress;
		if (portNumbers != null && !portNumbers.trim().isEmpty())
			result += ", portNumbers: " + portNumbers;
		return result;
	}
}