package com.infosys.infybenchpro.config;

import com.infosys.infybenchpro.entity.*;
import com.infosys.infybenchpro.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner { //TODO: need to remove

    private final AppUserRepository userRepo;
    private final EmployeeRepository employeeRepo;
    private final TaskRepository taskRepo;
    private final UserTaskRepository userTaskRepo;
    private final TaskTemplateRepository templateRepo;
    private final EmployeeGroupRepository groupRepo;
    private final EmployeeUpdateRepository updateRepo;
    private final BenchSpocMappingRepository benchSpocMappingRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedUsers();
        seedEmployees();
        seedBenchSpocMappings();
        seedTasks();
        seedTemplates();
        seedGroups();
        seedUpdates();
    }

    private void seedUsers() {
        if (userRepo.count() > 0) return;
        userRepo.save(new AppUser(null, "manager", passwordEncoder.encode("manager123"), Role.MANAGER, null, false));
    }

    private void seedEmployees() {
        if (employeeRepo.count() > 0) return;

        Employee e1 = new Employee();
        e1.setEmployeeId("1234567"); e1.setName("Sarah Johnson");
        e1.setEmail("sarah.johnson@company.com"); e1.setLocation("New York, NY");
        e1.setJobLevel("JL5");
        e1.setStatus(EmployeeStatus.PRODUCTION); e1.setRpl("Thomas Blank"); e1.setEmployeeDU("SAMPLE_ADM");
        e1.setCurrentCity("New York(NY)"); e1.setCurrentCountry("USA");
        e1.setTripCity("Hartford"); e1.setTripState("Connecticut");
        e1.setBenchSpoc("BENCH_SPOC_A");
        e1.setTechStreams(List.of(TechStream.DOTNET));
        e1.setSkills(List.of(
            new Skill("React", 5), new Skill("TypeScript", 4), new Skill("Angular", 3),
            new Skill("JavaScript", 7), new Skill(".NET / C#", 2), new Skill("CSS / Tailwind", 6)));
        Employee saved1 = employeeRepo.save(e1);

        // Create employee user linked to Sarah
        if (!userRepo.existsByUsername("employee")) {
            userRepo.save(new AppUser(null, "employee", passwordEncoder.encode("employee123"), Role.EMPLOYEE, saved1.getId(), false));
        }

        Employee e2 = new Employee();
        e2.setEmployeeId("2345678"); e2.setName("Michael Lee");
        e2.setEmail("michael.lee@company.com"); e2.setLocation("Chicago, IL");
        e2.setJobLevel("JL4");
        e2.setStatus(EmployeeStatus.PRODUCTION); e2.setRpl("Andrew Johnson"); e2.setEmployeeDU("ONE_ADM");
        e2.setCurrentCity("Chicago(IL)"); e2.setCurrentCountry("USA");
        e2.setTripCity("Phoenix"); e2.setTripState("Arizona");
        e2.setBenchSpoc("BENCH_SPOC_B");
        e2.setTechStreams(List.of(TechStream.JAVA));
        e2.setSkills(List.of(
            new Skill("Java", 6), new Skill("Spring Boot", 5), new Skill("SQL", 6),
            new Skill("Python", 3), new Skill("Kubernetes", 2), new Skill("REST APIs", 6)));
        employeeRepo.save(e2);

        Employee e3 = new Employee();
        e3.setEmployeeId("3456789"); e3.setName("Ava Smith");
        e3.setEmail("ava.smith@company.com"); e3.setLocation("San Francisco, CA");
        e3.setJobLevel("JL3A");
        e3.setStatus(EmployeeStatus.PRE_PRODUCTION); e3.setRpl("Thomas Blank"); e3.setEmployeeDU("TWO_ADM");
        e3.setCurrentCity("San Francisco(CA)"); e3.setCurrentCountry("USA");
        e3.setTripCity("Jersey City"); e3.setTripState("New Jersey");
        e3.setBenchSpoc("BENCH_SPOC_A");
        e3.setTechStreams(List.of(TechStream.BUSINESS_ANALYST));
        e3.setSkills(List.of(
            new Skill("Figma", 4), new Skill("UX Research", 3), new Skill("Agile / Scrum", 3),
            new Skill("SQL", 1), new Skill("Jira", 4)));
        employeeRepo.save(e3);

        Employee e4 = new Employee();
        e4.setEmployeeId("4567890"); e4.setName("James Brown");
        e4.setEmail("james.brown@company.com"); e4.setLocation("Austin, TX");
        e4.setJobLevel("JL3B");
        e4.setStatus(EmployeeStatus.BENCH); e4.setDaysOnBench(12);
        e4.setRpl("Andrew Johnson"); e4.setEmployeeDU("ONE_ADM");
        e4.setCurrentCity("Austin(TX)"); e4.setCurrentCountry("USA");
        e4.setTripCity("Dallas"); e4.setTripState("Texas");
        e4.setBenchSpoc("BENCH_SPOC_B");
        e4.setTechStreams(List.of(TechStream.JAVA));
        e4.setSkills(List.of(
            new Skill("Java", 3), new Skill("Selenium", 4), new Skill("Python", 2),
            new Skill("Jira", 5), new Skill("SQL", 3), new Skill("Postman / API Testing", 3)));
        employeeRepo.save(e4);

        Employee e5 = new Employee();
        e5.setEmployeeId("5678901"); e5.setName("Emily Davis");
        e5.setEmail("emily.davis@company.com"); e5.setLocation("Boston, MA");
        e5.setJobLevel("JL6");
        e5.setStatus(EmployeeStatus.PRODUCTION); e5.setRpl("Thomas Blank"); e5.setEmployeeDU("THREE_ADM");
        e5.setCurrentCity("Boston(MA)"); e5.setCurrentCountry("USA");
        e5.setTripCity("Providence"); e5.setTripState("Rhode Island");
        e5.setBenchSpoc("BENCH_SPOC_A");
        e5.setTechStreams(List.of(TechStream.DATA_SCIENCE));
        e5.setSkills(List.of(
            new Skill("Python", 8), new Skill("SQL", 7), new Skill("Machine Learning", 4),
            new Skill("Tableau", 5), new Skill("Spark / PySpark", 3), new Skill("R", 2)));
        employeeRepo.save(e5);

        seedBenchEmployees();
    }

    private void seedBenchEmployees() {
        Employee e6 = new Employee();
        e6.setEmployeeId("6789012"); e6.setName("David Kim");
        e6.setEmail("david.kim@company.com"); e6.setLocation("Seattle, WA");
        e6.setJobLevel("JL4");
        e6.setStatus(EmployeeStatus.BENCH); e6.setDaysOnBench(3);
        e6.setRpl("Andrew Johnson"); e6.setEmployeeDU("ONE_ADM");
        e6.setCurrentCity("Seattle(WA)"); e6.setCurrentCountry("USA");
        e6.setTripCity("Portland"); e6.setTripState("Oregon");
        e6.setBenchSpoc("BENCH_SPOC_B");
        e6.setTechStreams(List.of(TechStream.JAVA));
        e6.setSkills(List.of(new Skill("Java", 4), new Skill("Spring Boot", 3), new Skill("SQL", 4), new Skill("REST APIs", 4)));
        employeeRepo.save(e6);

        Employee e7 = new Employee();
        e7.setEmployeeId("7890123"); e7.setName("Maria Garcia");
        e7.setEmail("maria.garcia@company.com"); e7.setLocation("Dallas, TX");
        e7.setJobLevel("JL3A");
        e7.setStatus(EmployeeStatus.BENCH); e7.setDaysOnBench(7);
        e7.setRpl("Thomas Blank"); e7.setEmployeeDU("SAMPLE_ADM");
        e7.setCurrentCity("Dallas(TX)"); e7.setCurrentCountry("USA");
        e7.setTripCity("Houston"); e7.setTripState("Texas");
        e7.setBenchSpoc("BENCH_SPOC_A");
        e7.setTechStreams(List.of(TechStream.DOTNET));
        e7.setSkills(List.of(new Skill(".NET / C#", 2), new Skill("SQL", 2), new Skill("Azure", 1)));
        employeeRepo.save(e7);

        Employee e8 = new Employee();
        e8.setEmployeeId("8901234"); e8.setName("Kevin Patel");
        e8.setEmail("kevin.patel@company.com"); e8.setLocation("Denver, CO");
        e8.setJobLevel("JL3B");
        e8.setStatus(EmployeeStatus.BENCH); e8.setDaysOnBench(12);
        e8.setRpl("Andrew Johnson"); e8.setEmployeeDU("TWO_ADM");
        e8.setCurrentCity("Denver(CO)"); e8.setCurrentCountry("USA");
        e8.setTripCity("Colorado Springs"); e8.setTripState("Colorado");
        e8.setBenchSpoc("BENCH_SPOC_B");
        e8.setTechStreams(List.of(TechStream.JAVA));
        e8.setSkills(List.of(new Skill("Java", 3), new Skill("React", 2), new Skill("Python", 2), new Skill("SQL", 3)));
        employeeRepo.save(e8);

        Employee e9 = new Employee();
        e9.setEmployeeId("9012345"); e9.setName("Rachel Chen");
        e9.setEmail("rachel.chen@company.com"); e9.setLocation("Atlanta, GA");
        e9.setJobLevel("JL5");
        e9.setStatus(EmployeeStatus.BENCH); e9.setDaysOnBench(18);
        e9.setRpl("Thomas Blank"); e9.setEmployeeDU("THREE_ADM");
        e9.setCurrentCity("Atlanta(GA)"); e9.setCurrentCountry("USA");
        e9.setTripCity("Charlotte"); e9.setTripState("North Carolina");
        e9.setBenchSpoc("BENCH_SPOC_A");
        e9.setTechStreams(List.of(TechStream.DATA_SCIENCE));
        e9.setSkills(List.of(new Skill("Python", 5), new Skill("Machine Learning", 4), new Skill("SQL", 5), new Skill("Tableau", 3), new Skill("Spark / PySpark", 2)));
        employeeRepo.save(e9);

        Employee e10 = new Employee();
        e10.setEmployeeId("1023456"); e10.setName("Marcus Williams");
        e10.setEmail("marcus.williams@company.com"); e10.setLocation("Phoenix, AZ");
        e10.setJobLevel("JL4");
        e10.setStatus(EmployeeStatus.BENCH); e10.setDaysOnBench(25);
        e10.setRpl("Andrew Johnson"); e10.setEmployeeDU("ONE_ADM");
        e10.setCurrentCity("Phoenix(AZ)"); e10.setCurrentCountry("USA");
        e10.setTripCity("Scottsdale"); e10.setTripState("Arizona");
        e10.setBenchSpoc("BENCH_SPOC_B");
        e10.setTechStreams(List.of(TechStream.DOTNET));
        e10.setSkills(List.of(new Skill(".NET / C#", 5), new Skill("Azure", 3), new Skill("SQL", 4), new Skill("Angular", 2)));
        employeeRepo.save(e10);

        Employee e11 = new Employee();
        e11.setEmployeeId("1134567"); e11.setName("Priya Sharma");
        e11.setEmail("priya.sharma@company.com"); e11.setLocation("Portland, OR");
        e11.setJobLevel("JL3A");
        e11.setStatus(EmployeeStatus.BENCH); e11.setDaysOnBench(33);
        e11.setRpl("Thomas Blank"); e11.setEmployeeDU("SAMPLE_ADM");
        e11.setCurrentCity("Portland(OR)"); e11.setCurrentCountry("USA");
        e11.setTripCity("Eugene"); e11.setTripState("Oregon");
        e11.setBenchSpoc("BENCH_SPOC_A");
        e11.setTechStreams(List.of(TechStream.BUSINESS_ANALYST));
        e11.setSkills(List.of(new Skill("Jira", 3), new Skill("Agile / Scrum", 3), new Skill("SQL", 2), new Skill("Confluence", 2)));
        employeeRepo.save(e11);

        Employee e12 = new Employee();
        e12.setEmployeeId("1245678"); e12.setName("Tom Anderson");
        e12.setEmail("tom.anderson@company.com"); e12.setLocation("Minneapolis, MN");
        e12.setJobLevel("JL3B");
        e12.setStatus(EmployeeStatus.BENCH); e12.setDaysOnBench(42);
        e12.setRpl("Andrew Johnson"); e12.setEmployeeDU("TWO_ADM");
        e12.setCurrentCity("Minneapolis(MN)"); e12.setCurrentCountry("USA");
        e12.setTripCity("Saint Paul"); e12.setTripState("Minnesota");
        e12.setBenchSpoc("BENCH_SPOC_B");
        e12.setTechStreams(List.of(TechStream.JAVA));
        e12.setSkills(List.of(new Skill("Java", 3), new Skill("Selenium", 3), new Skill("SQL", 3), new Skill("Postman / API Testing", 2)));
        employeeRepo.save(e12);

        Employee e13 = new Employee();
        e13.setEmployeeId("1356789"); e13.setName("Lisa Nguyen");
        e13.setEmail("lisa.nguyen@company.com"); e13.setLocation("Nashville, TN");
        e13.setJobLevel("JL5");
        e13.setStatus(EmployeeStatus.BENCH); e13.setDaysOnBench(48);
        e13.setRpl("Thomas Blank"); e13.setEmployeeDU("THREE_ADM");
        e13.setCurrentCity("Nashville(TN)"); e13.setCurrentCountry("USA");
        e13.setTripCity("Memphis"); e13.setTripState("Tennessee");
        e13.setBenchSpoc("BENCH_SPOC_A");
        e13.setTechStreams(List.of(TechStream.DATA_SCIENCE));
        e13.setSkills(List.of(new Skill("Python", 7), new Skill("Machine Learning", 5), new Skill("R", 4), new Skill("Spark / PySpark", 3), new Skill("SQL", 6)));
        employeeRepo.save(e13);

        Employee e14 = new Employee();
        e14.setEmployeeId("1467890"); e14.setName("Carlos Rivera");
        e14.setEmail("carlos.rivera@company.com"); e14.setLocation("San Diego, CA");
        e14.setJobLevel("JL4");
        e14.setStatus(EmployeeStatus.BENCH); e14.setDaysOnBench(55);
        e14.setRpl("Andrew Johnson"); e14.setEmployeeDU("ONE_ADM");
        e14.setCurrentCity("San Diego(CA)"); e14.setCurrentCountry("USA");
        e14.setTripCity("Los Angeles"); e14.setTripState("California");
        e14.setBenchSpoc("BENCH_SPOC_B");
        e14.setTechStreams(List.of(TechStream.MAINFRAME));
        e14.setSkills(List.of(new Skill("COBOL", 6), new Skill("JCL", 5), new Skill("DB2", 4), new Skill("z/OS", 5)));
        employeeRepo.save(e14);

        Employee e15 = new Employee();
        e15.setEmployeeId("1578901"); e15.setName("Amy Thompson");
        e15.setEmail("amy.thompson@company.com"); e15.setLocation("Columbus, OH");
        e15.setJobLevel("JL3A");
        e15.setStatus(EmployeeStatus.BENCH); e15.setDaysOnBench(63);
        e15.setRpl("Thomas Blank"); e15.setEmployeeDU("SAMPLE_ADM");
        e15.setCurrentCity("Columbus(OH)"); e15.setCurrentCountry("USA");
        e15.setTripCity("Cleveland"); e15.setTripState("Ohio");
        e15.setBenchSpoc("BENCH_SPOC_A");
        e15.setTechStreams(List.of(TechStream.MAINFRAME));
        e15.setSkills(List.of(new Skill("COBOL", 3), new Skill("JCL", 2), new Skill("z/OS", 2)));
        employeeRepo.save(e15);
    }

    private void seedBenchSpocMappings() {
        if (benchSpocMappingRepo.count() > 0) return;

        // Map BENCH_SPOC_A → Sarah Johnson (employee 1), BENCH_SPOC_B → Michael Lee (employee 2)
        Employee sarah   = findByEmpId("1234567");
        Employee michael = findByEmpId("2345678");

        BenchSpocMapping m1 = new BenchSpocMapping();
        m1.setBenchSpoc("BENCH_SPOC_A");
        m1.setManagerId(sarah.getId());
        benchSpocMappingRepo.save(m1);

        BenchSpocMapping m2 = new BenchSpocMapping();
        m2.setBenchSpoc("BENCH_SPOC_B");
        m2.setManagerId(michael.getId());
        benchSpocMappingRepo.save(m2);

        // Now cascade managerId to all employees based on their benchSpoc
        employeeRepo.findAll().forEach(emp -> {
            if (emp.getBenchSpoc() != null) {
                benchSpocMappingRepo.findByBenchSpoc(emp.getBenchSpoc())
                    .ifPresent(mapping -> {
                        emp.setManagerId(mapping.getManagerId());
                        employeeRepo.save(emp);
                    });
            }
        });
    }

    private void seedTasks() {
        if (taskRepo.count() > 0) return;

        Employee sarah   = findByEmpId("1234567");
        Employee michael = findByEmpId("2345678");
        Employee ava     = findByEmpId("3456789");
        Employee james   = findByEmpId("4567890");

        Task t101 = taskRepo.save(makeTask("Prepare release notes",
            "Compile and review release notes for the current sprint.",
            TaskPriority.MEDIUM, List.of(sarah.getId(), james.getId())));
        seedUserTask(t101, sarah.getId(),   TaskStatus.IN_PROGRESS, "2026-04-10");
        seedUserTask(t101, james.getId(),   TaskStatus.IN_PROGRESS, "2026-04-10");

        Task t102 = taskRepo.save(makeTask("Fix login timeout issue",
            "Investigate and resolve timeout issue on login flow.",
            TaskPriority.HIGH, List.of(michael.getId())));
        seedUserTask(t102, michael.getId(), TaskStatus.COMPLETED,   "2026-03-25");

        Task t103 = taskRepo.save(makeTask("Review onboarding screens",
            "Audit current onboarding experience and propose updates.",
            TaskPriority.LOW, List.of(ava.getId())));
        seedUserTask(t103, ava.getId(),     TaskStatus.COMPLETED,   "2026-03-30");

        Task t108 = taskRepo.save(makeTask("Client portal sprint kickoff",
            "Lead initial component breakdown and assign tickets to the team.",
            TaskPriority.HIGH, List.of(sarah.getId())));
        seedUserTask(t108, sarah.getId(),   TaskStatus.IN_PROGRESS, "2026-04-15");

        Task t109 = taskRepo.save(makeTask("API rate-limit load testing",
            "Run load tests against staging after deploying rate-limiting middleware.",
            TaskPriority.MEDIUM, List.of(michael.getId())));
        seedUserTask(t109, michael.getId(), TaskStatus.IN_PROGRESS, "2026-04-12");

        Task t110 = taskRepo.save(makeTask("Bench skills assessment",
            "Complete the internal skills assessment form while on bench.",
            TaskPriority.MEDIUM, List.of(james.getId())));
        seedUserTask(t110, james.getId(),   TaskStatus.IN_PROGRESS, "2026-04-18");

        // Template-ready base tasks (unassigned)
        taskRepo.save(makeTask("Complete HR paperwork",
            "Fill out all required HR forms, benefits enrollment, and tax documents.",
            TaskPriority.HIGH, List.of()));
        taskRepo.save(makeTask("Set up workstation",
            "Configure laptop, install required software, and set up accounts.",
            TaskPriority.HIGH, List.of()));
        taskRepo.save(makeTask("Read company handbook",
            "Review company policies, culture guide, and code of conduct.",
            TaskPriority.MEDIUM, List.of()));
        taskRepo.save(makeTask("Schedule intro meetings",
            "Set up 1:1 meetings with your manager and key team members.",
            TaskPriority.LOW, List.of()));
    }

    private Task makeTask(String title, String desc, TaskPriority priority, List<Long> empIds) {
        Task t = new Task();
        t.setTitle(title);
        t.setDescription(desc);
        t.setPriority(priority);
        t.setAssignedEmployeeIds(new java.util.ArrayList<>(empIds));
        return t;
    }

    private void seedUserTask(Task task, Long empId, TaskStatus status, String dueDate) {
        UserTask ut = new UserTask();
        ut.setTask(task);
        ut.setEmployeeId(empId);
        ut.setStatus(status);
        ut.setDueDate(LocalDate.parse(dueDate));
        userTaskRepo.save(ut);
    }

    private void seedTemplates() {
        if (templateRepo.count() > 0) return;

        List<Task> tasks = taskRepo.findAll();
        Long hrId        = findTaskByTitle(tasks, "Complete HR paperwork");
        Long wsId        = findTaskByTitle(tasks, "Set up workstation");
        Long handbookId  = findTaskByTitle(tasks, "Read company handbook");
        Long meetingsId  = findTaskByTitle(tasks, "Schedule intro meetings");

        TaskTemplate tmpl = new TaskTemplate();
        tmpl.setName("New Employee Onboarding");
        tmpl.setDescription("Standard checklist for onboarding a new team member.");
        tmpl.setTasks(List.of(
            new TemplateTask(hrId, 1),
            new TemplateTask(wsId, 2),
            new TemplateTask(handbookId, 5),
            new TemplateTask(meetingsId, 7)
        ));
        templateRepo.save(tmpl);
    }

    private void seedGroups() {
        if (groupRepo.count() > 0) return;

        Employee sarah   = findByEmpId("1234567");
        Employee michael = findByEmpId("2345678");
        Employee ava     = findByEmpId("3456789");
        Employee james   = findByEmpId("4567890");

        EmployeeGroup g1 = new EmployeeGroup();
        g1.setName("Engineering");
        g1.setDescription("Frontend and backend engineering team members.");
        g1.setEmployeeIds(List.of(sarah.getId(), michael.getId()));
        groupRepo.save(g1);

        EmployeeGroup g2 = new EmployeeGroup();
        g2.setName("Design & QA");
        g2.setDescription("Design and quality assurance team members.");
        g2.setEmployeeIds(List.of(ava.getId(), james.getId()));
        groupRepo.save(g2);
    }

    private void seedUpdates() {
        if (updateRepo.count() > 0) return;

        Employee sarah   = findByEmpId("1234567");
        Employee michael = findByEmpId("2345678");
        Employee ava     = findByEmpId("3456789");
        Employee james   = findByEmpId("4567890");
        Employee emily   = findByEmpId("5678901");

        saveUpdate(sarah.getId(),   "Finished the dashboard redesign and handed off to QA. Waiting on sign-off before merging.", "2026-04-01");
        saveUpdate(sarah.getId(),   "Resolved the mobile layout regression on the settings page. Also reviewed two PRs from the backend team.", "2026-03-28");
        saveUpdate(sarah.getId(),   "Kicked off the client portal sprint. Initial component breakdown done, starting implementation Monday.", "2026-03-24");
        saveUpdate(michael.getId(), "Login timeout bug is isolated — session token refresh wasn't being triggered on idle. Fix is in review.", "2026-04-01");
        saveUpdate(michael.getId(), "Deployed the new API rate-limiting middleware to staging. Load tests look good so far.", "2026-03-26");
        saveUpdate(ava.getId(),     "Completed the onboarding screen audit. Shared a Figma with 12 proposed improvements — ready for review.", "2026-03-30");
        saveUpdate(ava.getId(),     "Wrapped up the icon refresh for the mobile app. All assets exported and handed to engineering.", "2026-03-25");
        saveUpdate(james.getId(),   "Currently blocked on release notes — waiting on Michael to finalize the API changelog section.", "2026-03-31");
        saveUpdate(emily.getId(),   "Vendor onboarding checklist is 80% done. Expecting to close out the remaining items by end of week.", "2026-03-29");

        Employee e6  = findByEmpId("6789012");
        Employee e7  = findByEmpId("7890123");
        Employee e8  = findByEmpId("8901234");
        Employee e9  = findByEmpId("9012345");
        Employee e10 = findByEmpId("1023456");
        Employee e11 = findByEmpId("1134567");
        Employee e12 = findByEmpId("1245678");
        Employee e13 = findByEmpId("1356789");
        Employee e14 = findByEmpId("1467890");
        Employee e15 = findByEmpId("1578901");

        saveUpdate(e6.getId(),  "Completed the internal Java refresher course. Ready for placement.", "2026-04-05");
        saveUpdate(e7.getId(),  "Finished .NET certification exam prep. Awaiting client opportunity.", "2026-04-03");
        saveUpdate(e8.getId(),  "Started a self-directed microservices course using Spring Boot.", "2026-04-02");
        saveUpdate(e9.getId(),  "Completed an internal ML pipeline PoC for the data team.", "2026-04-01");
        saveUpdate(e10.getId(), "Studying for Azure Solutions Architect certification while on bench.", "2026-03-31");
        saveUpdate(e11.getId(), "Attended internal Agile coaching workshop. Updated resume and skills profile.", "2026-03-28");
        saveUpdate(e12.getId(), "Revamped internal Selenium test suite. Reduced flakiness by 40%.", "2026-03-25");
        saveUpdate(e13.getId(), "Built a demo Spark pipeline for the pre-sales team. Presentation went well.", "2026-03-20");
        saveUpdate(e14.getId(), "Reviewing COBOL modernization patterns. Flagged a few legacy migration risks.", "2026-03-12");
        saveUpdate(e15.getId(), "Completed mainframe basics refresher. No placement matches found yet.", "2026-03-04");
    }

    private void saveUpdate(Long empId, String message, String date) {
        EmployeeUpdate u = new EmployeeUpdate();
        u.setEmployeeId(empId);
        u.setMessage(message);
        u.setDate(LocalDate.parse(date));
        updateRepo.save(u);
    }

    private Employee findByEmpId(String employeeId) {
        return employeeRepo.findByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + employeeId));
    }

    private Long findTaskByTitle(List<Task> tasks, String title) {
        return tasks.stream()
                .filter(t -> t.getTitle().equals(title))
                .findFirst()
                .map(Task::getId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + title));
    }
}
