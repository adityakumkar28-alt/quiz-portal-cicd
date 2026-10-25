package com.devops.quiz;

import com.devops.quiz.model.QuizQuestion;
import com.devops.quiz.repository.QuizQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class QuizPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizPortalApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedDatabase(QuizQuestionRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                List<QuizQuestion> defaultQuestions = Arrays.asList(
                        new QuizQuestion(
                                "DevOps & CI/CD",
                                "In Continuous Integration (CI), what is the primary goal of running automated tests on every code commit?",
                                "To detect integration bugs early and provide immediate feedback",
                                "To replace manual code reviews completely",
                                "To speed up the deployment to production without testing",
                                "To generate marketing documentation automatically",
                                1,
                                4
                        ),
                        new QuizQuestion(
                                "Software Architecture",
                                "Which architectural pattern organizes an application into loosely coupled, independently deployable services communicating over lightweight protocols?",
                                "Monolithic Architecture",
                                "Microservices Architecture",
                                "Mainframe Processing",
                                "Shared-Database Cluster",
                                2,
                                4
                        ),
                        new QuizQuestion(
                                "Containerization",
                                "In Docker, what is the primary purpose of a multi-stage build in a Dockerfile?",
                                "To run multiple Docker containers in parallel",
                                "To execute build steps as root without permissions",
                                "To optimize image size by separating build tools from the final runtime image",
                                "To automatically generate Kubernetes manifest files",
                                3,
                                4
                        ),
                        new QuizQuestion(
                                "Version Control",
                                "What happens during a fast-forward Git merge?",
                                "A new merge commit with two parents is always created",
                                "The branch pointer simply moves forward to point to the incoming commit",
                                "All previous commits are squashed into a single commit",
                                "The remote repository is forcefully overwritten",
                                2,
                                4
                        ),
                        new QuizQuestion(
                                "Cloud & Infrastructure",
                                "What is the core principle of Infrastructure as Code (IaC)?",
                                "Managing infrastructure through manual console configurations",
                                "Storing server passwords in plaintext scripts",
                                "Managing and provisioning compute infrastructure through machine-readable definition files",
                                "Restricting cloud deployments to a single vendor hardware appliance",
                                3,
                                4
                        )
                );
                repository.saveAll(defaultQuestions);
            }
        };
    }
}
