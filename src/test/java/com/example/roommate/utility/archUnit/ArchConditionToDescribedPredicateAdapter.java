package com.example.roommate.utility.archUnit;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;

public class ArchConditionToDescribedPredicateAdapter extends DescribedPredicate<JavaClass> {
    private final ArchCondition<JavaClass> archCondition;

    ArchConditionToDescribedPredicateAdapter(ArchCondition<JavaClass> archCondition) {
        super(archCondition.getDescription());
        this.archCondition = archCondition;
    }
    public static DescribedPredicate<JavaClass> create(ArchCondition<JavaClass> condition){
        return new ArchConditionToDescribedPredicateAdapter(condition);
    }

    @Override
    public boolean test(JavaClass javaClass) {
        ConditionEvents conditionEvents = ConditionEvents.Factory.create();
        archCondition.check(javaClass, conditionEvents);
        return !conditionEvents.containViolation();
    }
    
    

    // You might need to override other methods like asString() if necessary
}
