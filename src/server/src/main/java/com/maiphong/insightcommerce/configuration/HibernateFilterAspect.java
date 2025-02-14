package com.maiphong.insightcommerce.configuration;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.maiphong.insightcommerce.core.constants.CommonConstant;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class HibernateFilterAspect {

    private static final Logger logger = LoggerFactory.getLogger(HibernateFilterAspect.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Before("@within(transactional) || @annotation(transactional)")
    public void enableDeletedFilter(Transactional transactional) {
        Session session = entityManager.unwrap(Session.class);
        if (session.getEnabledFilter(CommonConstant.DELETED_FILTER) == null) {
            session.enableFilter(CommonConstant.DELETED_FILTER);
            logger.debug("Hibernate filter 'deletedFilter' enabled.");
        } else {
            logger.debug("Hibernate filter 'deletedFilter' was already enabled.");
        }
    }
}
