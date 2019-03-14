package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * @author Pierre Gabioud (247216)
 * @author Corto Callerisa (251769)
 * 
 * L'entité représentant une relation OSM.
 */
public final class OSMRelation extends OSMEntity {
	private final List<Member> members;

	/**
	 * @param id
	 *            l'identifiant de la relation OSM en construction
	 * 
	 * @param members
	 *            les membres de la relation OSM en construction
	 * 
	 * @param attributes
	 *            les attributs de la relation OSM en construction
	 */
	public OSMRelation(long id, List<Member> members, Attributes attributes) {
		super(id, attributes);
		this.members = Collections.unmodifiableList(new ArrayList<>(members));   
	}

	/**
	 * @return la liste des membres de la relation
	 */
	public List<Member> members() {
		return members;
	}

	/**
	 * Représente un membre d'une relation OSM
	 */
	public final static class Member {
		private final Type type;
		private final String role;
		private final OSMEntity member;

		/**
		 * @param type
		 *            le type du membre
		 * 
		 * @param role
		 *            le role du membre
		 * 
		 * @param member
		 *            la valeur du membre
		 */
		public Member(Type type, String role, OSMEntity member) {
			this.type = type;
			this.role = role;
			this.member = member;
		}

		/**
		 * @return le type du membre
		 */
		public Type type() {
			return type;
		}

		/**
		 * @return le role du membre
		 */
		public String role() {
			return role;
		}

		/**
		 * @return le membre
		 */
		public OSMEntity member() {
			return member;
		}

		/**
		 * enumeration des trois types de membres qu'une relation peut comporter
		 */
		public enum Type {
			NODE, WAY, RELATION
		};

	}

	/**
	 * batisseur de la classe OSMRelation
	 */
	public final static class Builder extends OSMEntity.Builder {
		private final List<Member> members = new ArrayList<>();

		/**
		 * @param id
		 *            l'identifiant de la relation en construction
		 */
		public Builder(long id) {
			super(id);
		}

		/**
		 * @param type
		 *            le type du membre donne à la relation
		 * 
		 * @param role
		 *            le role du membre donne à la relation
		 * 
		 * @param newMember
		 *            le nouveau membre donne à la relation
		 * 
		 *            ajoute un nouveau membre de type et de role donnes à la
		 *            relation
		 */
		public void addMember(Member.Type type, String role, OSMEntity newMember) {
			members.add(new Member(type, role, newMember));                             //Member member = new Member(type, role, newMember);
		}                                                                               //optimisation approuved by PGM
		                                    
		/**
		 * @return construit la relation ayant l'identifiant passe au
		 *         constructeur ainsi que les membres et les attributs ajoutes
		 * 
		 * @throws IllegalStateException
		 *             si la relation en cours de construction est incomplete
		 */
		public OSMRelation build() throws IllegalStateException {
			if (isIncomplete()) {
				throw new IllegalStateException();
			}
			long id = super.id;
			Attributes attributes = super.attributes.build();
			return new OSMRelation(id, members, attributes);
		}
	}
}
