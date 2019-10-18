package com.societegenerale.commons.plugin.rules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.societegenerale.commons.plugin.utils.ArchUtils;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Returning null collections (List, Set) forces the caller to always perform a
 * null check, which hinders readability. It's much better to never return a
 * null Collection, and instead return an empty one. This rule enforces that all
 * methods returning a Collection must be annotated with @Nonnull
 *
 * @see: there is no agreed standard for notNull annotation, see <a href=
 *       "https://stackoverflow.com/questions/4963300/which-notnull-java-annotation-should-i-use/">here</a>
 *
 */
public class DontReturnNullCollectionTest implements ArchRuleTest {

	protected static final String NO_NULL_COLLECTION_MESSAGE = "we don't want callers to perform null check every time. Return an empty collection, not null.";

	@Override
	public void execute(String path) {

		// Using JavaClass to get SubClasses

		JavaClass javaClassCollection = new ClassFileImporter().importClass(Collection.class);

		DescribedPredicate<JavaMethod> returnACollectionSubClass = new DescribedPredicate<JavaMethod>(
				"return a Collection SubClass") {
			@Override
			public boolean apply(JavaMethod method) {

				Set<JavaClass> javaClasscollectionSubClasses = javaClassCollection.getAllSubClasses();

				// A Set containing the equivalent using reflect

				Set<Class<?>> equivalentToJavaClasscollectionSubClasses = javaClasscollectionSubClasses.parallelStream()
						.map(JavaClass::reflect).collect(Collectors.toSet());

				return equivalentToJavaClasscollectionSubClasses.contains(method.getRawReturnType().reflect());

			}
		};

		ArchRule rule = methods().that().haveRawReturnType(List.class).or().haveRawReturnType(Set.class).should()
				.beAnnotatedWith(Nonnull.class).because(NO_NULL_COLLECTION_MESSAGE);

		ArchRule ruleUsingCollectionSubClasses = methods().that(returnACollectionSubClass).should()
				.beAnnotatedWith(Nonnull.class).because(NO_NULL_COLLECTION_MESSAGE);

		ruleUsingCollectionSubClasses.check(ArchUtils.importAllClassesInPackage(path, SRC_CLASSES_FOLDER));
	}

}