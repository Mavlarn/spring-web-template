package com.mtt.myapp.common.model;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.mtt.myapp.common.excpetion.CustomRuntimeException;
import com.mtt.myapp.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Base Entity. This has a long type ID field
 * 
 * @param <M>
 *            wrapped entity type
 * 
 * @author Liu Zhifei
 * @author Mavlarn
 * @since 1.0
 */
@MappedSuperclass
public class BaseEntity<M> implements Serializable {

	private static final long serialVersionUID = 8571113820348514692L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "created_date", insertable = true, updatable = false)
	private Date createdDate;

	@ManyToOne
	@JoinColumn(name = "created_user", insertable = true, updatable = false)
	private User createdUser;

	@Column(name = "last_modified_date", insertable = true, updatable = true)
	private Date lastModifiedDate;

	@ManyToOne
	@JoinColumn(name = "last_modified_user", insertable = true, updatable = true)
	private User lastModifiedUser;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(User createdUser) {
		this.createdUser = createdUser;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public User getLastModifiedUser() {
		return lastModifiedUser;
	}

	public void setLastModifiedUser(User lastModifiedUser) {
		this.lastModifiedUser = lastModifiedUser;
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	/**
	 * This function is used to check whether the entity id exist. It is not used to check the
	 * entity existence in DB. It can be used to check the entity in controller, which is passed
	 * from page.
	 * 
	 * @return true if exists
	 */
	public boolean exist() {
		return id != null && id.longValue() != 0;
	}

	/**
	 * Merge source entity into current entity.
	 * 
	 * Only not null value is merged.
	 * 
	 * @param source
	 *            merge source
	 * @return merged entity
	 */
	@SuppressWarnings("unchecked")
	public M merge(M source) {
		PropertyDescriptor forError = null;
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(getClass());
			// Iterate over all the attributes
			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
				forError = descriptor;
				// Only copy writable attributes
				Method writeMethod = descriptor.getWriteMethod();
				if (writeMethod == null) {
					continue;
				}
				// Only copy values values where the source values is not null
				Method readMethod = descriptor.getReadMethod();
				if (readMethod == null) {
					continue;
				}

				Object defaultValue = readMethod.invoke(source);
				if (defaultValue == null) {
					continue;
				}

				if (isNotBlankStringOrNotString(defaultValue)) {
					writeMethod.invoke(this, defaultValue);
				}
			}
			return (M) this;
		} catch (Exception e) {
			String displayName = (forError == null) ? "Empty" : forError.getDisplayName();
			throw new CustomRuntimeException(displayName + " - Exception occurs while merging entities from "
							+ source + " to " + this, e);
		}
	}

	private boolean isNotBlankStringOrNotString(Object aValue) {
		boolean isNotBlankString = aValue instanceof String && StringUtils.isNotBlank((String) aValue);
		boolean isNotString = !(aValue instanceof String);
		return isNotBlankString || isNotString;
	}
}
