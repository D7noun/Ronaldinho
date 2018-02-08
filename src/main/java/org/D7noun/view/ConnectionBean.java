package org.D7noun.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.D7noun.model.Connection;

/**
 * Backing bean for Connection entities.
 * <p/>
 * This class provides CRUD functionality for all Connection entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
@ViewScoped
public class ConnectionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving Connection entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Connection connection;

	public Connection getConnection() {
		return this.connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "ronaldinho", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.connection = this.example;
		} else {
			this.connection = findById(getId());
		}
	}

	public Connection findById(Long id) {

		return this.entityManager.find(Connection.class, id);
	}

	/*
	 * Support updating and deleting Connection entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.connection);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.connection);
				return "view?faces-redirect=true&id=" + this.connection.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			Connection deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching Connection entities with pagination
	 */

	private int page;
	private long count;
	private List<Connection> pageItems;

	private Connection example = new Connection();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Connection getExample() {
		return this.example;
	}

	public void setExample(Connection example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Connection> root = countCriteria.from(Connection.class);
		countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria).getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<Connection> criteria = builder.createQuery(Connection.class);
		root = criteria.from(Connection.class);
		TypedQuery<Connection> query = this.entityManager
				.createQuery(criteria.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<Connection> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String connectionName = this.example.getConnectionName();
		if (connectionName != null && !"".equals(connectionName)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("connectionName")),
					'%' + connectionName.toLowerCase() + '%'));
		}
		String ipAddress = this.example.getIpAddress();
		if (ipAddress != null && !"".equals(ipAddress)) {
			predicatesList.add(
					builder.like(builder.lower(root.<String>get("ipAddress")), '%' + ipAddress.toLowerCase() + '%'));
		}
		String macAddress = this.example.getMacAddress();
		if (macAddress != null && !"".equals(macAddress)) {
			predicatesList.add(
					builder.like(builder.lower(root.<String>get("macAddress")), '%' + macAddress.toLowerCase() + '%'));
		}
		String portNumbers = this.example.getPortNumbers();
		if (portNumbers != null && !"".equals(portNumbers)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("portNumbers")),
					'%' + portNumbers.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Connection> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Connection entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Connection> getAll() {

		CriteriaQuery<Connection> criteria = this.entityManager.getCriteriaBuilder().createQuery(Connection.class);
		return this.entityManager.createQuery(criteria.select(criteria.from(Connection.class))).getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final ConnectionBean ejbProxy = this.sessionContext.getBusinessObject(ConnectionBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context, UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((Connection) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Connection add = new Connection();

	public Connection getAdd() {
		return this.add;
	}

	public Connection getAdded() {
		Connection added = this.add;
		this.add = new Connection();
		return added;
	}
}
