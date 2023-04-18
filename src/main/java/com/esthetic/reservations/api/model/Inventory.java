package com.esthetic.reservations.api.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory extends BaseModel<Inventory> {
	private String inventory_name;
	
	private Double price;
	
	private Long store;
	
	private String imagen;
	
	private String description;
	
	private String capacibility;
	
	@JoinColumn(name = "id_branch")
	@ManyToOne
	private Branch idBranch;

	public Inventory() {
		super();
	}

	public Inventory(Long id, String inventory_name, Double price, Long store, String imagen, Branch id_branch, String description, String capacibility) {
		super(id);
		this.inventory_name = inventory_name;
		this.price = price;
		this.store = store;
		this.idBranch = id_branch;
		this.imagen = imagen;
		this.description = description;
		this.capacibility = capacibility;
	}

	public Inventory(Inventory other) {
		this.setId(other.getId());
		this.inventory_name = other.inventory_name;
		this.price = other.price;
		this.store = other.store;
		this.idBranch = other.idBranch;
		this.imagen = other.imagen;
		this.description = other.description;
		this.capacibility = other.capacibility;
	}
	
	public Inventory(String inventory_name, Double price, Long store, String imagen, Branch id_branch, String description, String capacibility) {
		super();
		this.inventory_name = inventory_name;
		this.price = price;
		this.store = store;
		this.idBranch = id_branch;
		this.imagen = imagen;
		this.description = description;
		this.capacibility = capacibility;
	}

	public String getInventory_name() {
		return inventory_name;
	}

	public void setInventory_name(String inventory_name) {
		this.inventory_name = inventory_name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getStore() {
		return store;
	}

	public void setStore(Long store) {
		this.store = store;
	}

	public Branch getId_branch() {
		return idBranch;
	}

	public void setId_branch(Branch id_branch) {
		this.idBranch = id_branch;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCapacibility() {
		return capacibility;
	}

	public void setCapacibility(String capacibility) {
		this.capacibility = capacibility;
	}

	@Override
	public void copy(Inventory object) {
		this.inventory_name = object.inventory_name;
		this.price = object.price;
		this.store = object.store;
		this.idBranch = object.idBranch;
		this.imagen = object.imagen;
		this.description = object.description;
		this.capacibility = object.capacibility;
	}

    @Override
    public String toString() {
        return "Inventory [inventory_name=" + inventory_name + ", price=" + price + ", store=" + store + ", imagen="
                + imagen + ",description=" + description + ", capacibility=" + capacibility + "idBranch=" + idBranch + "]";
    }
	
	
	
	
}
