package com.lms.config;

import java.util.Arrays;
import java.util.List;

import com.lms.model.BookDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.couchbase.repository.config.RepositoryOperationsMapping;


import com.couchbase.client.java.Bucket;


@Configuration
@EnableCouchbaseRepositories(basePackages={"com.lms"})
public class LmsCouchbaseConfig extends AbstractCouchbaseConfiguration{

    public static final List<String> NODE_LIST = Arrays.asList("localhost");
    public static final String DEFAULT_BUCKET_NAME = "student";
    public static final String DEFAULT_BUCKET_PASSWORD = "student12";

    @Override
    protected List<String> getBootstrapHosts() {
        return NODE_LIST;
    }

    @Override
    protected String getBucketName() {
        return DEFAULT_BUCKET_NAME;
    }

    @Override
    protected String getBucketPassword() {
        return DEFAULT_BUCKET_PASSWORD;
    }

    @Bean
    public Bucket bookBucket() throws Exception {
        return couchbaseCluster().openBucket("book", "book123");
    }

    @Bean(name = "bookTemplate")
    public CouchbaseTemplate bookTemplate() throws Exception {
        CouchbaseTemplate template = new CouchbaseTemplate(couchbaseClusterInfo(), bookBucket(), mappingCouchbaseConverter(), translationService());
        template.setDefaultConsistency(getDefaultConsistency());
        return template;
    }

    @Override
    public void configureRepositoryOperationsMapping(RepositoryOperationsMapping baseMapping) {
        try {
            baseMapping.mapEntity(BookDetails.class, bookTemplate() );
        } catch (Exception e) {
            // custom Exception handling
        }
    }

}
