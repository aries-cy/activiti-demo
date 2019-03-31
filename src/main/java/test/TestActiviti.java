package test;

import org.activiti.engine.*;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class TestActiviti {

    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    @Test
    public void test() {
        //以下两种方式选择一种创建引擎方式：1.配置写在程序里 2.读对应的配置文件
        //1
//        testCreateProcessEngine();
        //2
        //创建引擎
//        testCreateProcessEngineByCfgXml();
        //发布流程
        deployProcess();
        //启动流程
        startProcess();
        //查询任务
        queryTask();
        //handleTask();

    }

    @Test
    public void testCreateProcessEngine() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        cfg.setJdbcDriver("com.mysql.jdbc.Driver");
        cfg.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti");
        cfg.setJdbcUsername("root");
        cfg.setJdbcPassword("root");
        //配置建表策略
        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine engine = cfg.buildProcessEngine();
    }


    /**
     * 创建引擎
     */
    @Test
    public void testCreateProcessEngineByCfgXml() {
        ProcessEngineConfiguration cfg = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine engine = cfg.buildProcessEngine();
    }

    /**
     * 发布流程
     */
    @Test
    public void deployProcess(){
        RepositoryService repositoryService = processEngine.getRepositoryService();
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addClasspathResource("demo.bpmn");
        builder.deploy();
    }

    /**
     * 启动流程
     */
    @Test
    public void startProcess(){
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("myProcess_1");
    }

    /**
     * 查看任务
     */
    @Test
    public void queryTask(){
        TaskService taskService = processEngine.getTaskService();
        String student = "student";
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(student).list();
        for (Task task : tasks) {
            System.out.println("taskId:" + task.getId() +
                    ",taskName:" + task.getName() +
                    ",assignee:" + task.getAssignee() +
                    ",createTime:" + task.getCreateTime());
        }
    }

    /**
     * 办理任务
     */
    @Test
    public void handleTask(){
        TaskService taskService = processEngine.getTaskService();
        //根据上一步生成的taskId执行任务
        String taskId = "20008";
        taskService.complete(taskId);
    }
}
