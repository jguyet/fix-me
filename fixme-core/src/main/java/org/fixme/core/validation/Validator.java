package org.fixme.core.validation;

import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.ProviderSpecificBootstrap;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;

public class Validator {

	private static ProviderSpecificBootstrap<HibernateValidatorConfiguration> psb = Validation.byProvider(HibernateValidator.class);
	private static Configuration<?> configuration = psb.configure();
	
	private static ValidatorFactory factory = configuration.buildValidatorFactory();//Validation.buildDefaultValidatorFactory();
	private static javax.validation.Validator validator = factory.getValidator();

	public static boolean validateObject(Object e) {
		
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(e);
		 
		if (constraintViolations.size() > 0 ) {
			System.out.println("\033[31m{");
			System.out.println("	Impossible de valider les donnees du bean <" + e.getClass().getName() + "> : Error information -> ");
			for (ConstraintViolation<Object> contraintes : constraintViolations) {
				System.out.println("	" + contraintes.getPropertyPath() + " " + contraintes.getMessage() + "");
			}
			System.out.println("}\033[00m");
			return false;
		}
		System.out.println("\033[32m{Les donnees du bean <" + e.getClass().getName() + "> sont valides}\033[00m");
		return true;
	}
}
