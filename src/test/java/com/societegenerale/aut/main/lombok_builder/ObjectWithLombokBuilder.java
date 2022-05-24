package com.societegenerale.aut.main.lombok_builder;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;

import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.Data;

@Data
public class ObjectWithLombokBuilder {

	private final String id;

	ObjectWithLombokBuilder(String id) {
		this.id = id;
	}

	public static ObjectWithLombokBuilderBuilder builder() {
		return new ObjectWithLombokBuilderBuilder();
	}

	@Nonnull
	public List returningANonNullList(){
		return emptyList();
	}

	@Nonnull
	public Set returningANonNullSet(){
		return emptySet();
	}

	public static class ObjectWithLombokBuilderBuilder {

		private String id;

		ObjectWithLombokBuilderBuilder() {
		}

		public ObjectWithLombokBuilderBuilder id(String id) {
			this.id = id;
			return this;
		}

		public ObjectWithLombokBuilder build() {
			return new ObjectWithLombokBuilder(id);
		}

		public String toString() {
			return "ObjectWithLombokBuilder.ObjectWithLombokBuilderBuilder(id=" + this.id + ")";
		}
	}
}
