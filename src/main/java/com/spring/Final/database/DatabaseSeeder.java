package com.spring.Final.database;

import com.github.javafaker.Faker;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.employee.EmployeeEntity;
import com.spring.Final.modules.employee.EmployeeRepository;
import com.spring.Final.modules.employee_skill.EmployeeSkillEntity;
import com.spring.Final.modules.employee_skill.EmployeeSkillRepository;
import com.spring.Final.modules.employer.EmployerEntity;
import com.spring.Final.modules.employer.EmployerRepository;
import com.spring.Final.modules.job_category.JobCategoryEntity;
import com.spring.Final.modules.job_category.JobCategoryRepository;
import com.spring.Final.modules.job_category_employee.JobCategoryEmployeeEntity;
import com.spring.Final.modules.job_category_employee.JobCategoryEmployeeRepository;
import com.spring.Final.modules.job_proposal.JobProposalEntity;
import com.spring.Final.modules.job_proposal.JobProposalRepository;
import com.spring.Final.modules.job_skill.JobSkillEntity;
import com.spring.Final.modules.job_skill.JobSkillRepository;
import com.spring.Final.modules.job_type.JobTypeEntity;
import com.spring.Final.modules.job_type.JobTypeRepository;
import com.spring.Final.modules.jobs.JobEntity;
import com.spring.Final.modules.jobs.JobRepository;
import com.spring.Final.modules.membership.MembershipEntity;
import com.spring.Final.modules.membership.MembershipRepository;
import com.spring.Final.modules.review.ReviewEntity;
import com.spring.Final.modules.review.ReviewRepository;
import com.spring.Final.modules.shared.enums.job_proposal_status.JobProposalStatus;
import com.spring.Final.modules.shared.enums.job_proposal_type.JobProposalType;
import com.spring.Final.modules.shared.enums.job_status.JobStatus;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import com.spring.Final.modules.shared.enums.membership_type.MembershipType;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import com.spring.Final.modules.skill.SkillEntity;
import com.spring.Final.modules.skill.SkillRepository;
import org.locationtech.jts.io.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DatabaseSeeder {
    private final Faker faker;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final JobTypeRepository jobTypeRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final EmployerRepository employerRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final MembershipRepository membershipRepository;
    private final ReviewRepository reviewRepository;
    private final JobProposalRepository jobProposalRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final JobCategoryEmployeeRepository jobCategoryEmployeeRepository;
    private final JobSkillRepository jobSkillRepository;

    @Autowired
    public DatabaseSeeder(
            BCryptPasswordEncoder passwordEncoder,
            EmployeeRepository employeeRepository,
            JobTypeRepository jobTypeRepository,
            JobCategoryRepository jobCategoryRepository,
            EmployerRepository employerRepository,
            JobRepository jobRepository,
            SkillRepository skillRepository,
            MembershipRepository membershipRepository,
            ReviewRepository reviewRepository,
            JobProposalRepository jobProposalRepository,
            EmployeeSkillRepository employeeSkillRepository,
            JobCategoryEmployeeRepository jobCategoryEmployeeRepository,
            JobSkillRepository jobSkillRepository
    ) {
        this.faker = new Faker();
        this.passwordEncoder = passwordEncoder;
        this.employeeRepository = employeeRepository;
        this.jobTypeRepository = jobTypeRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.employerRepository = employerRepository;
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.membershipRepository = membershipRepository;
        this.reviewRepository = reviewRepository;
        this.jobProposalRepository = jobProposalRepository;
        this.employeeSkillRepository = employeeSkillRepository;
        this.jobCategoryEmployeeRepository = jobCategoryEmployeeRepository;
        this.jobSkillRepository = jobSkillRepository;
    }

//    @EventListener
    public void seed(ContextRefreshedEvent event) throws ParseException {
        this.fakeEmployees();
        this.fakeJobTypes();
        this.fakeJobCategories();
        this.fakeEmployers();
        this.fakeJobs();
        this.fakeSkills();
        this.fakeMemberships();
        this.fakeProposals();
        this.fakeReviews();
        this.fakeJobSkill();
        this.fakeEmployeeSkill();
        this.fakeJobCategoryEmployee();
    }

    public void fakeEmployees() throws ParseException {
        List<EmployeeEntity> employees = new ArrayList<>();

        // add default employees
        EmployeeEntity defaultEmployee = new EmployeeEntity();
        defaultEmployee.setFirstName("Rowan");
        defaultEmployee.setLastName("Atkinson");
        defaultEmployee.setPhone(faker.phoneNumber().cellPhone());
        defaultEmployee.setEmail("employee@yopmail.com");
        defaultEmployee.setPassword(this.passwordEncoder.encode("123"));
        defaultEmployee.setVerified(true);
        defaultEmployee.setSlug(CommonHelper.toSlug("employee@yopmail.com"));
        defaultEmployee.setAddressLocation(
                CommonHelper.createGeometryPoint(
                        Double.parseDouble(faker.address().latitude()),
                        Double.parseDouble(faker.address().longitude())
                )
        );
        defaultEmployee.setDescription(getString(20));
        employees.add(defaultEmployee);

        for (int i = 0; i < 20; i++) {
            String email = faker.internet().emailAddress();

            EmployeeEntity employee = new EmployeeEntity();
            employee.setFirstName(faker.name().firstName());
            employee.setLastName(faker.name().lastName());
            employee.setPhone(faker.phoneNumber().cellPhone());
            employee.setEmail(email);
            employee.setPassword(this.passwordEncoder.encode("123"));
            employee.setSlug(CommonHelper.toSlug(email));
            defaultEmployee.setDescription(getString(20));
            employee.setAddressLocation(
                    CommonHelper.createGeometryPoint(Double.parseDouble(faker.address().latitude()), Double.parseDouble(faker.address().longitude()))
            );
            employees.add(employee);
        }

        this.employeeRepository.saveAll(employees);
    }

    public void fakeJobTypes() {
        List<JobTypeEntity> jobTypes = new ArrayList<>();

        jobTypes.add(new JobTypeEntity("Freelance"));
        jobTypes.add(new JobTypeEntity("Full Time"));
        jobTypes.add(new JobTypeEntity("Part Time"));
        jobTypes.add(new JobTypeEntity("Internship"));
        jobTypes.add(new JobTypeEntity("Temporary"));

        this.jobTypeRepository.saveAll(jobTypes);
    }

    public void fakeJobCategories() {
        List<JobCategoryEntity> jobCategories = new ArrayList<>();

        jobCategories.add(new JobCategoryEntity("Accounting and Finance", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Clerical & Data Entry", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Counseling", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Court Administration", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Human Resources", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Investigative", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("IT and Computers", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Law Enforcement", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Management", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Miscellaneous", getString(7), "icon-line-awesome-file-code-o"));
        jobCategories.add(new JobCategoryEntity("Public Relations", getString(7), "icon-line-awesome-file-code-o"));

        this.jobCategoryRepository.saveAll(jobCategories);
    }

    public void fakeEmployers() {
        String[] images = new String[]{
                "https://www.brandchannel.com/wp-content/uploads/2016/10/tesla-logo-500-300x300.jpg",
                "http://www.vasterad.com/themes/hireo/images/company-logo-01.png",
                "http://www.vasterad.com/themes/hireo/images/company-logo-02.png",
                "http://www.vasterad.com/themes/hireo/images/company-logo-03.png",
                "http://www.vasterad.com/themes/hireo/images/company-logo-04.png",
                "http://www.vasterad.com/themes/hireo/images/company-logo-05.png",
                "http://www.vasterad.com/themes/hireo/images/company-logo-06.png",
        };
        List<EmployerEntity> employers = new ArrayList<>();

        // add default employees
        EmployerEntity defaultEmployer = new EmployerEntity();
        defaultEmployer.setName("Tesla");
        defaultEmployer.setPhone(faker.phoneNumber().cellPhone());
        defaultEmployer.setEmail("employer@yopmail.com");
        defaultEmployer.setPassword(this.passwordEncoder.encode("123"));
        defaultEmployer.setAvatar(images[0]);
        defaultEmployer.setVerified(true);
        defaultEmployer.setDescription(getString(20));
        defaultEmployer.setSlug(CommonHelper.toSlug("employer@yopmail.com"));
        employers.add(defaultEmployer);

        for (int i = 1; i < 7; i++) {
            String email = faker.internet().emailAddress();

            EmployerEntity employer = new EmployerEntity();
            employer.setName(faker.name().name());
            employer.setPhone(faker.phoneNumber().cellPhone());
            employer.setEmail(email);
            employer.setPassword(this.passwordEncoder.encode("123"));
            employer.setAvatar(images[i]);
            employer.setVerified(true);
            defaultEmployer.setDescription(getString(20));
            employer.setSlug(CommonHelper.toSlug(email));
            employers.add(employer);
        }

        this.employerRepository.saveAll(employers);
    }

    public void fakeJobs() {
        List<JobEntity> jobs = new ArrayList<>();
        List<EmployerEntity> employers = this.employerRepository.findAll();
        List<JobTypeEntity> jobTypes = this.jobTypeRepository.findAll();
        List<JobCategoryEntity> jobCategories = this.jobCategoryRepository.findAll();

        for (int i = 0; i < 50; i++) {
            String name = getString(3);

            JobEntity job = new JobEntity();
            job.setEmployer((EmployerEntity) randomElement(employers));
            job.setJobCategory((JobCategoryEntity) randomElement(jobCategories));
            job.setJobType((JobTypeEntity) randomElement(jobTypes));
            job.setName(name);

            if (faker.random().nextBoolean()) {
                int salaryFrom = faker.random().nextInt(0, 10) * 1000;
                job.setSalaryFrom(BigDecimal.valueOf(salaryFrom));
                job.setSalaryTo(BigDecimal.valueOf(salaryFrom * 10));
            }
            job.setDescription(String.join("\n", faker.lorem().paragraphs(5)));
            job.setAddress(faker.address().fullAddress());
            job.setAddressLocation(
                    CommonHelper.createGeometryPoint(Double.parseDouble(faker.address().latitude()), Double.parseDouble(faker.address().longitude()))
            );
            job.setStatus((JobStatus) randomElement(Arrays.asList(JobStatus.values())));
            job.setExpiredAt(
                    faker.date().between(
                            Date.from(LocalDate.parse("2020-12-01").atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                            Date.from(LocalDate.parse("2021-06-01").atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
                    )
            );
            job.setSlug(CommonHelper.toSlug(name));

            jobs.add(job);
        }

        this.jobRepository.saveAll(jobs);
    }

    public void fakeSkills() {
        List<JobCategoryEntity> categories = this.jobCategoryRepository.findAll();
        JobCategoryEntity itDepartment = null;
        JobCategoryEntity accountingDepartment = null;
        JobCategoryEntity dataEntryDepartment = null;
        JobCategoryEntity counselingDepartment = null;
        JobCategoryEntity courtAdministrationDepartment = null;
        JobCategoryEntity humanResourcesDepartment = null;
        JobCategoryEntity investigativeDepartment = null;
        JobCategoryEntity lawEnforcementDepartment = null;
        JobCategoryEntity managementDepartment = null;
        JobCategoryEntity miscellaneousDepartment = null;
        JobCategoryEntity publicRelationsDepartment = null;

        for (JobCategoryEntity category : categories) {
            if (category.getName().equals("IT and Computers")) {
                itDepartment = category;
            }
            if (category.getName().equals("Accounting and Finance")) {
                accountingDepartment = category;
            }
            if (category.getName().equals("Clerical & Data Entry")) {
                dataEntryDepartment = category;
            }
            if (category.getName().equals("Counseling")) {
                counselingDepartment = category;
            }
            if (category.getName().equals("Court Administration")) {
                courtAdministrationDepartment = category;
            }
            if (category.getName().equals("Human Resources")) {
                humanResourcesDepartment = category;
            }
            if (category.getName().equals("Investigative")) {
                investigativeDepartment = category;
            }
            if (category.getName().equals("Law Enforcement")) {
                lawEnforcementDepartment = category;
            }
            if (category.getName().equals("Management")) {
                managementDepartment = category;
            }
            if (category.getName().equals("Miscellaneous")) {
                miscellaneousDepartment = category;
            }
            if (category.getName().equals("Public Relations")) {
                publicRelationsDepartment = category;
            }
        }

        List<SkillEntity> skills = new ArrayList<>();

        skills.add(new SkillEntity("Ruby on Rails", itDepartment));
        skills.add(new SkillEntity("Symfony", itDepartment));
        skills.add(new SkillEntity("Angular JS", itDepartment));
        skills.add(new SkillEntity("React.js", itDepartment));
        skills.add(new SkillEntity("Cake PHP", itDepartment));
        skills.add(new SkillEntity("Asp.net", itDepartment));
        skills.add(new SkillEntity("Node.js", itDepartment));
        skills.add(new SkillEntity("Yii Framework", itDepartment));
        skills.add(new SkillEntity("Meteor", itDepartment));
        skills.add(new SkillEntity("Laravel", itDepartment));
        skills.add(new SkillEntity("Ember", itDepartment));
        skills.add(new SkillEntity("Django", itDepartment));
        skills.add(new SkillEntity("Express.js", itDepartment));
        skills.add(new SkillEntity("Spring", itDepartment));
        skills.add(new SkillEntity("Flask", itDepartment));
        skills.add(new SkillEntity("Active Server Pages", itDepartment));
        skills.add(new SkillEntity("jQuery", itDepartment));
        skills.add(new SkillEntity("CodeIgniter", itDepartment));
        skills.add(new SkillEntity("Drupal", itDepartment));
        skills.add(new SkillEntity("Bootstrap", itDepartment));
        skills.add(new SkillEntity("web2py", itDepartment));
        skills.add(new SkillEntity("TurboGears", itDepartment));
        skills.add(new SkillEntity("Sinatra", itDepartment));
        skills.add(new SkillEntity("Zend Framework", itDepartment));
        skills.add(new SkillEntity("CherryPy", itDepartment));
        skills.add(new SkillEntity("Phalcon", itDepartment));
        skills.add(new SkillEntity("Sails.js", itDepartment));
        skills.add(new SkillEntity("Pylons web framework", itDepartment));
        skills.add(new SkillEntity("Apache Struts 1", itDepartment));
        skills.add(new SkillEntity("PLAY", itDepartment));
        skills.add(new SkillEntity("FuelPHP", itDepartment));
        skills.add(new SkillEntity("Grails", itDepartment));
        skills.add(new SkillEntity("Bottle", itDepartment));
        skills.add(new SkillEntity("Mojolicious", itDepartment));
        skills.add(new SkillEntity("Apache Wicket", itDepartment));
        skills.add(new SkillEntity("Catalyst", itDepartment));
        skills.add(new SkillEntity("Scalatra", itDepartment));
        skills.add(new SkillEntity("MooTools", itDepartment));
        skills.add(new SkillEntity("Wt", itDepartment));
        skills.add(new SkillEntity("PRADO", itDepartment));
        skills.add(new SkillEntity("Grok", itDepartment));
        skills.add(new SkillEntity("Yesod", itDepartment));
        skills.add(new SkillEntity("Padrino", itDepartment));
        skills.add(new SkillEntity("Quixote", itDepartment));
        skills.add(new SkillEntity("Lift", itDepartment));
        skills.add(new SkillEntity("Mason", itDepartment));
        skills.add(new SkillEntity("Apache Cocoon", itDepartment));
        skills.add(new SkillEntity("Fat-Free Framework", itDepartment));
        skills.add(new SkillEntity("Stripes", itDepartment));
        skills.add(new SkillEntity("Seaside", itDepartment));
        skills.add(new SkillEntity("JHipster", itDepartment));

        skills.add(new SkillEntity("A formal accounting qualification", accountingDepartment));
        skills.add(new SkillEntity("Interpersonal skills", accountingDepartment));
        skills.add(new SkillEntity("Ability to communicate", accountingDepartment));
        skills.add(new SkillEntity("Financial reporting", accountingDepartment));
        skills.add(new SkillEntity("Analytical ability", accountingDepartment));
        skills.add(new SkillEntity("Problem-solving skills", accountingDepartment));
        skills.add(new SkillEntity("Knowledge of IT software", accountingDepartment));
        skills.add(new SkillEntity("Management experience", accountingDepartment));

        skills.add(new SkillEntity("Typing", dataEntryDepartment));
        skills.add(new SkillEntity("Data Entry Experience", dataEntryDepartment));
        skills.add(new SkillEntity("Attention to Detail", dataEntryDepartment));
        skills.add(new SkillEntity("Communication Skills", dataEntryDepartment));
        skills.add(new SkillEntity("Customer Service", dataEntryDepartment));
        skills.add(new SkillEntity("Clerical Skills", dataEntryDepartment));
        skills.add(new SkillEntity("MS Office", dataEntryDepartment));
        skills.add(new SkillEntity("Receptionist", dataEntryDepartment));
        skills.add(new SkillEntity("Administrative Assistant", dataEntryDepartment));
        skills.add(new SkillEntity("Written Communication", dataEntryDepartment));

        skills.add(new SkillEntity("Relationship Building", counselingDepartment));
        skills.add(new SkillEntity("Social Media", counselingDepartment));
        skills.add(new SkillEntity("Psychology", counselingDepartment));
        skills.add(new SkillEntity("Sales Experience", counselingDepartment));
        skills.add(new SkillEntity("Mental Health", counselingDepartment));
        skills.add(new SkillEntity("Recruiting", counselingDepartment));
        skills.add(new SkillEntity("Adult Education", counselingDepartment));
        skills.add(new SkillEntity("Receptionist", counselingDepartment));
        skills.add(new SkillEntity("Trauma", counselingDepartment));
        skills.add(new SkillEntity("Clinic", counselingDepartment));

        skills.add(new SkillEntity("Court Proceedings", courtAdministrationDepartment));
        skills.add(new SkillEntity("Bench Warrants", courtAdministrationDepartment));
        skills.add(new SkillEntity("Legal Documents", courtAdministrationDepartment));
        skills.add(new SkillEntity("Food Court", courtAdministrationDepartment));
        skills.add(new SkillEntity("General Public", courtAdministrationDepartment));
        skills.add(new SkillEntity("Domestic Violence", courtAdministrationDepartment));
        skills.add(new SkillEntity("Law Enforcement", courtAdministrationDepartment));
        skills.add(new SkillEntity("Court Records", courtAdministrationDepartment));
        skills.add(new SkillEntity("Telephone Calls", courtAdministrationDepartment));
        skills.add(new SkillEntity("Subpoenas", courtAdministrationDepartment));
        skills.add(new SkillEntity("Scheduling Hearings", courtAdministrationDepartment));
        skills.add(new SkillEntity("Community Resources", courtAdministrationDepartment));
        skills.add(new SkillEntity("Small Claims", courtAdministrationDepartment));
        skills.add(new SkillEntity("Food Preparation", courtAdministrationDepartment));
        skills.add(new SkillEntity("Judicial Officer", courtAdministrationDepartment));
        skills.add(new SkillEntity("Child Support", courtAdministrationDepartment));
        skills.add(new SkillEntity("New Cases", courtAdministrationDepartment));
        skills.add(new SkillEntity("Costco", courtAdministrationDepartment));
        skills.add(new SkillEntity("Court Staff", courtAdministrationDepartment));
        skills.add(new SkillEntity("Computer System", courtAdministrationDepartment));

        skills.add(new SkillEntity("Employee relations", humanResourcesDepartment));
        skills.add(new SkillEntity("Onboarding", humanResourcesDepartment));
        skills.add(new SkillEntity("Human Resources Information Software (HRIS)", humanResourcesDepartment));
        skills.add(new SkillEntity("Performance management", humanResourcesDepartment));
        skills.add(new SkillEntity("Teamwork and collaboration", humanResourcesDepartment));
        skills.add(new SkillEntity("Scheduling", humanResourcesDepartment));
        skills.add(new SkillEntity("Customer service", humanResourcesDepartment));
        skills.add(new SkillEntity("Project management", humanResourcesDepartment));

        skills.add(new SkillEntity("Law Enforcement", investigativeDepartment));
        skills.add(new SkillEntity("Evidence Collection", investigativeDepartment));
        skills.add(new SkillEntity("Search Warrants", investigativeDepartment));
        skills.add(new SkillEntity("Surveillance Operations", investigativeDepartment));
        skills.add(new SkillEntity("Crime Prevention", investigativeDepartment));
        skills.add(new SkillEntity("Criminal Activity", investigativeDepartment));
        skills.add(new SkillEntity("Investigative Reports", investigativeDepartment));
        skills.add(new SkillEntity("Firearms", investigativeDepartment));
        skills.add(new SkillEntity("Court Proceedings", investigativeDepartment));
        skills.add(new SkillEntity("Subpoenas", investigativeDepartment));
        skills.add(new SkillEntity("Internal Investigations", investigativeDepartment));
        skills.add(new SkillEntity("Undercover Operations", investigativeDepartment));
        skills.add(new SkillEntity("Legal Documents", investigativeDepartment));
        skills.add(new SkillEntity("Federal Agencies", investigativeDepartment));
        skills.add(new SkillEntity("Background Investigations", investigativeDepartment));
        skills.add(new SkillEntity("Documentary Evidence", investigativeDepartment));
        skills.add(new SkillEntity("Government Officials", investigativeDepartment));
        skills.add(new SkillEntity("IRS", investigativeDepartment));
        skills.add(new SkillEntity("DEA", investigativeDepartment));
        skills.add(new SkillEntity("DHS", investigativeDepartment));

        skills.add(new SkillEntity("Police Department", lawEnforcementDepartment));
        skills.add(new SkillEntity("Law Enforcement", lawEnforcementDepartment));
        skills.add(new SkillEntity("Crime Prevention", lawEnforcementDepartment));
        skills.add(new SkillEntity("Federal Laws", lawEnforcementDepartment));
        skills.add(new SkillEntity("Public Safety", lawEnforcementDepartment));
        skills.add(new SkillEntity("Firearms", lawEnforcementDepartment));
        skills.add(new SkillEntity("Criminal Activity", lawEnforcementDepartment));
        skills.add(new SkillEntity("Present Evidence", lawEnforcementDepartment));
        skills.add(new SkillEntity("Traffic Laws", lawEnforcementDepartment));
        skills.add(new SkillEntity("Search Warrants", lawEnforcementDepartment));
        skills.add(new SkillEntity("CPR", lawEnforcementDepartment));
        skills.add(new SkillEntity("Domestic Violence", lawEnforcementDepartment));
        skills.add(new SkillEntity("Motor Vehicle", lawEnforcementDepartment));
        skills.add(new SkillEntity("Traffic Accidents", lawEnforcementDepartment));
        skills.add(new SkillEntity("General Public", lawEnforcementDepartment));
        skills.add(new SkillEntity("Traffic Control", lawEnforcementDepartment));
        skills.add(new SkillEntity("Criminal Acts", lawEnforcementDepartment));
        skills.add(new SkillEntity("Full Range", lawEnforcementDepartment));
        skills.add(new SkillEntity("Court Proceedings", lawEnforcementDepartment));
        skills.add(new SkillEntity("Subpoenas", lawEnforcementDepartment));

        skills.add(new SkillEntity("Project Management", managementDepartment));
        skills.add(new SkillEntity("Procedures", managementDepartment));
        skills.add(new SkillEntity("Infrastructure", managementDepartment));
        skills.add(new SkillEntity("Architecture", managementDepartment));
        skills.add(new SkillEntity("Hardware", managementDepartment));
        skills.add(new SkillEntity("Technical Support", managementDepartment));
        skills.add(new SkillEntity("Customer Service", managementDepartment));
        skills.add(new SkillEntity("Product Development", managementDepartment));
        skills.add(new SkillEntity("Business Requirements", managementDepartment));
        skills.add(new SkillEntity("Application Development", managementDepartment));
        skills.add(new SkillEntity("Business Development", managementDepartment));
        skills.add(new SkillEntity("C++", managementDepartment));
        skills.add(new SkillEntity("C #", managementDepartment));
        skills.add(new SkillEntity("Troubleshoot", managementDepartment));
        skills.add(new SkillEntity("R", managementDepartment));
        skills.add(new SkillEntity("Java", managementDepartment));
        skills.add(new SkillEntity("New Technologies", managementDepartment));
        skills.add(new SkillEntity("Facility", managementDepartment));
        skills.add(new SkillEntity("Internet", managementDepartment));
        skills.add(new SkillEntity("Preventative Maintenance", managementDepartment));

        skills.add(new SkillEntity("Customer Service", miscellaneousDepartment));
        skills.add(new SkillEntity("Technical Support", miscellaneousDepartment));
        skills.add(new SkillEntity("Hardware", miscellaneousDepartment));
        skills.add(new SkillEntity("Executive Support", miscellaneousDepartment));
        skills.add(new SkillEntity("Desktop", miscellaneousDepartment));
        skills.add(new SkillEntity("CRM", miscellaneousDepartment));
        skills.add(new SkillEntity("Remote Access", miscellaneousDepartment));
        skills.add(new SkillEntity("Setup", miscellaneousDepartment));
        skills.add(new SkillEntity("Mac", miscellaneousDepartment));
        skills.add(new SkillEntity("Active Directory", miscellaneousDepartment));
        skills.add(new SkillEntity("Laptops", miscellaneousDepartment));
        skills.add(new SkillEntity("Troubleshoot", miscellaneousDepartment));
        skills.add(new SkillEntity("Apple", miscellaneousDepartment));
        skills.add(new SkillEntity("OS", miscellaneousDepartment));
        skills.add(new SkillEntity("Data Centers", miscellaneousDepartment));
        skills.add(new SkillEntity("Android", miscellaneousDepartment));
        skills.add(new SkillEntity("C-Level", miscellaneousDepartment));
        skills.add(new SkillEntity("Windows", miscellaneousDepartment));
        skills.add(new SkillEntity("Blackberry", miscellaneousDepartment));
        skills.add(new SkillEntity("Procedures", miscellaneousDepartment));

        skills.add(new SkillEntity("Press Releases", publicRelationsDepartment));
        skills.add(new SkillEntity("Company Website", publicRelationsDepartment));
        skills.add(new SkillEntity("Special Events", publicRelationsDepartment));
        skills.add(new SkillEntity("Facebook", publicRelationsDepartment));
        skills.add(new SkillEntity("Promotional Materials", publicRelationsDepartment));
        skills.add(new SkillEntity("Trade Shows", publicRelationsDepartment));
        skills.add(new SkillEntity("Twitter", publicRelationsDepartment));
        skills.add(new SkillEntity("Community Events", publicRelationsDepartment));
        skills.add(new SkillEntity("Radio Station", publicRelationsDepartment));
        skills.add(new SkillEntity("External Communications", publicRelationsDepartment));
        skills.add(new SkillEntity("Community Relations", publicRelationsDepartment));
        skills.add(new SkillEntity("General Public", publicRelationsDepartment));
        skills.add(new SkillEntity("Potential Clients", publicRelationsDepartment));
        skills.add(new SkillEntity("Instagram", publicRelationsDepartment));
        skills.add(new SkillEntity("Internal Communications", publicRelationsDepartment));
        skills.add(new SkillEntity("Fact Sheets", publicRelationsDepartment));
        skills.add(new SkillEntity("Public Relations Strategies", publicRelationsDepartment));
        skills.add(new SkillEntity("Community Outreach", publicRelationsDepartment));
        skills.add(new SkillEntity("Promotional Events", publicRelationsDepartment));
        skills.add(new SkillEntity("Local Businesses", publicRelationsDepartment));

        this.skillRepository.saveAll(skills);
    }

    public void fakeMemberships() {
        List<MembershipEntity> memberships = new ArrayList<>();

        MembershipEntity standard = new MembershipEntity();
        standard.setName("Standard");
        standard.setType(MembershipType.STANDARD);
        standard.setDuration(MembershipDuration.MONTHLY);
        standard.setPrice(BigDecimal.valueOf(50));
        standard.setDescription(getString(20));
        standard.setCreatedAt(CommonHelper.getCurrentTime());
        standard.setUpdatedAt(CommonHelper.getCurrentTime());
        memberships.add(standard);

        MembershipEntity plus = new MembershipEntity();
        plus.setName("Plus");
        plus.setType(MembershipType.PLUS);
        plus.setDuration(MembershipDuration.MONTHLY);
        plus.setPrice(BigDecimal.valueOf(100));
        plus.setDescription(getString(20));
        plus.setCreatedAt(CommonHelper.getCurrentTime());
        plus.setUpdatedAt(CommonHelper.getCurrentTime());
        memberships.add(plus);

        MembershipEntity enterprise = new MembershipEntity();
        enterprise.setName("Enterprise");
        enterprise.setType(MembershipType.ENTERPRISE);
        enterprise.setDuration(MembershipDuration.MONTHLY);
        enterprise.setPrice(BigDecimal.valueOf(200));
        enterprise.setDescription(getString(20));
        enterprise.setCreatedAt(CommonHelper.getCurrentTime());
        enterprise.setUpdatedAt(CommonHelper.getCurrentTime());
        memberships.add(enterprise);

        // yearly
        MembershipEntity standardYearly = new MembershipEntity();
        standardYearly.setName("Standard");
        standardYearly.setType(MembershipType.STANDARD);
        standardYearly.setDuration(MembershipDuration.YEARLY);
        standardYearly.setPrice(BigDecimal.valueOf(500));
        standardYearly.setDescription(getString(20));
        standardYearly.setCreatedAt(CommonHelper.getCurrentTime());
        standardYearly.setUpdatedAt(CommonHelper.getCurrentTime());
        memberships.add(standardYearly);

        MembershipEntity plusYearly = new MembershipEntity();
        plusYearly.setName("Plus");
        plusYearly.setType(MembershipType.PLUS);
        plusYearly.setDuration(MembershipDuration.YEARLY);
        plusYearly.setPrice(BigDecimal.valueOf(1000));
        plusYearly.setDescription(getString(20));
        plusYearly.setCreatedAt(CommonHelper.getCurrentTime());
        plusYearly.setUpdatedAt(CommonHelper.getCurrentTime());
        memberships.add(plusYearly);

        MembershipEntity enterpriseYearly = new MembershipEntity();
        enterpriseYearly.setName("Enterprise");
        enterpriseYearly.setType(MembershipType.ENTERPRISE);
        enterpriseYearly.setDuration(MembershipDuration.YEARLY);
        enterpriseYearly.setPrice(BigDecimal.valueOf(2000));
        enterpriseYearly.setDescription(getString(20));
        enterpriseYearly.setCreatedAt(CommonHelper.getCurrentTime());
        enterpriseYearly.setUpdatedAt(CommonHelper.getCurrentTime());
        memberships.add(enterpriseYearly);

        this.membershipRepository.saveAll(memberships);
    }

    public void fakeProposals() {
        List<EmployeeEntity> employees = this.employeeRepository.findAll();
        List<JobEntity> jobs = this.jobRepository.findAll();
        List<JobProposalEntity> jobProposals = new ArrayList<>();

        for (EmployeeEntity employee : employees) {
            for (JobEntity job : randomNElements(jobs, 3)) {
                JobProposalEntity proposal = new JobProposalEntity();
                proposal.setJob(job);
                proposal.setEmployee(employee);
                proposal.setType(JobProposalType.PROPOSAL);
                proposal.setStatus((JobProposalStatus) randomElement(Arrays.asList(new JobProposalStatus[]{JobProposalStatus.PENDING, JobProposalStatus.REJECTED, JobProposalStatus.ACCEPTED})));
                proposal.setMessage(getString(10));

                jobProposals.add(proposal);
            }
        }

        this.jobProposalRepository.saveAll(jobProposals);
    }

    public void fakeReviews() {
        List<ReviewEntity> reviews = new ArrayList<>();
        List<JobProposalEntity> jobProposals = this.jobProposalRepository.findAll();

        for (JobProposalEntity jobProposal : jobProposals) {
            String message = String.join(" ", faker.lorem().words(3));

            ReviewEntity review = new ReviewEntity();
            review.setJobProposal(jobProposal);
            review.setUserId(jobProposal.getJob().getEmployer().getId());
            review.setUserType(UserType.EMPLOYER);
            review.setToUserId(jobProposal.getEmployee().getId());
            review.setToUserType(UserType.EMPLOYEE);
            review.setMessage(message);
            review.setRating(faker.random().nextInt(1, 5));
            review.setDeliveredOnBudget(faker.random().nextBoolean());
            review.setDeliveredOnTime(faker.random().nextBoolean());
            review.setCreatedAt(CommonHelper.getCurrentTime());

            reviews.add(review);
        }

        this.reviewRepository.saveAll(reviews);
    }

    public void fakeJobSkill() {
        List<SkillEntity> skills = this.skillRepository.findAll();
        List<JobEntity> jobs = this.jobRepository.findAll();
        List<JobSkillEntity> jobsSkills = new ArrayList<>();

        for (JobEntity job : jobs) {
            List<SkillEntity> categorySkills = skills.stream().filter(e -> e.getJobCategory().getId().equals(job.getJobCategory().getId())).collect(Collectors.toList());
            List<SkillEntity> jobSkills = randomNElements(categorySkills, 6);

            for (SkillEntity jobSkill : jobSkills) {
                jobsSkills.add(new JobSkillEntity(job, jobSkill));
            }
        }

        this.jobSkillRepository.saveAll(jobsSkills);
    }

    public void fakeEmployeeSkill() {
        List<SkillEntity> skills = this.skillRepository.findAll();
        List<EmployeeEntity> employees = this.employeeRepository.findAll();
        List<EmployeeSkillEntity> employeesSkills = new ArrayList<>();

        for (EmployeeEntity employee : employees) {
            List<SkillEntity> employeeSkills = randomNElements(skills, 10);

            for (SkillEntity employeeSkill : employeeSkills) {
                employeesSkills.add(new EmployeeSkillEntity(employee, employeeSkill));
            }
        }

        this.employeeSkillRepository.saveAll(employeesSkills);
    }

    public void fakeJobCategoryEmployee() {
        List<JobCategoryEntity> jobCategories = this.jobCategoryRepository.findAll();
        List<EmployeeEntity> employees = this.employeeRepository.findAll();
        List<JobCategoryEmployeeEntity> employeesCategories = new ArrayList<>();

        for (EmployeeEntity employee : employees) {
            List<JobCategoryEntity> employeeSkills = randomNElements(jobCategories, 10);

            for (JobCategoryEntity employeeCategory : employeeSkills) {
                employeesCategories.add(new JobCategoryEmployeeEntity(employeeCategory, employee));
            }
        }

        this.jobCategoryEmployeeRepository.saveAll(employeesCategories);
    }

    private <T> List<T> randomNElements(List<T> list, int n) {
        List<T> copy = new LinkedList<>(list);
        Collections.shuffle(copy);

        return copy.subList(0, n);
    }

    private <T> Object randomElement(List<T> elements) {
        if (elements.isEmpty()) {
            return null;
        }
        int randIndex = faker.random().nextInt(0, elements.size() - 1);

        return elements.get(randIndex);
    }

    private String getString(int charNumber) {
        return String.join(" ", faker.lorem().words(charNumber));
    }
}
