package nc.ukma.thor.spms.entity;

import java.util.ArrayList;
import java.util.List;

public class TraitCategory {
	
	private Short id;
	private String name;
	private List<Trait> traits = new ArrayList<Trait>();
	
	public TraitCategory() {}
	
	public TraitCategory(Short traitCategoryId){
		this.id = traitCategoryId;
	}
	
	public TraitCategory(Short id, String name) {
		this.id = id;
		this.name = name;
	}

	public TraitCategory(String name) {
		this.name = name;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Trait> getTraits() {
		return traits;
	}

	public void setTraits(List<Trait> traits) {
		this.traits = traits;
	}
	
	public void addTrait(Trait trait) {
		trait.setTraitCategory(new TraitCategory(this.getId()));
		traits.add(trait);
	}

	@Override
	public String toString() {
		return "TraitCategory [id=" + id + ", name=" + name + ", traits=" + traits + "]";
	}

	
	
}
