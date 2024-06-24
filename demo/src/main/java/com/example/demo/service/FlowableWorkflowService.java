package com.example.demo.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FlowableWorkflowService {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	
	public void startAllFlows() {
		
		int[] empId = {5,10,50,100,250,500,1000};
		
		// int[] empId = {1};
		
		for (int i = 0; i < empId.length; i++) {
			
			int task_number = empId[i];
			
			String resourceName = "sequentialFlow_"+task_number+".bpmn";
			String stanceName = "sequentialProcess_" + task_number;
			
			//String resourceName = "flowable-wind113.bpmn";
		    //String stanceName = "process3";
			
		    long startTime = 0;
		
			removePreviousDeploys(resourceName);
				
			// deploy flow
			startTime = System.currentTimeMillis();
			repositoryService.createDeployment().name(resourceName).addClasspathResource(resourceName).deploy();
			computerExecTime(startTime, "deploy of " + resourceName + " done: ");
			
			Map<String, Object> variables = new HashMap<>();
			variables.put("var_test", generateRandomString(128));
			//variables.put("'pass'", "yes");
			
			// create a stance
			startTime = System.currentTimeMillis();
			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(stanceName,variables);
			computerExecTime(startTime, "stance of " + stanceName + " with id= " + processInstance.getId() + " started: ");
			
			for( Task t : taskService.createTaskQuery().list() ) {
				System.out.println(t.getName());
			}
			
	
			System.out.println("isSuspended: " + processInstance.isSuspended());
			System.out.println("isEnded: " + processInstance.isEnded());
			
			
			startTime = System.currentTimeMillis();
			
			do {
	            List<Task> tasks = taskService.createTaskQuery()
	                    .processInstanceId(processInstance.getId())
	                    .active()
	                    .list();

	            for (Task task : tasks) {
	                System.out.println("current task: " + task.getName());
	                
	               /// Map<String, Object> variables2 = new HashMap<>();
	    		   // variables2.put("'pass'", "yes");            
	                taskService.complete(task.getId()); // variables2
	                
	               
	            }
	        } while (taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().count() > 0);

		
			computerExecTime(startTime, "finish tasks flow id= " + processInstance.getId() +" ");
			System.out.println("isSuspended: " + processInstance.isSuspended());
			System.out.println("isEnded: " + processInstance.isEnded());
			
		}
		
		
	}


	private void removePreviousDeploys(String resourceName) {
		List<Deployment> dps = repositoryService.createDeploymentQuery().deploymentName(resourceName).list();

		// Se já existe, exclui o deployment existente
		for (Deployment existingDeployment : dps) {
			if (existingDeployment != null) {
				// O segundo parâmetro indica se as instâncias de processo relacionadas devem ser removidas
				repositoryService.deleteDeployment(existingDeployment.getId(), true); 
				System.out.println("deploy de " + resourceName + " está sendo removido para ser atualizado");
			}

		}
	}
	

	public void computerExecTime(long startTime, String action) {
		long endTime = System.currentTimeMillis();
	    long executionTime = endTime - startTime;
		System.out.println(action + executionTime + " ms");
	}
	
	 private static String generateRandomString(int length) {
	        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	        StringBuilder randomString = new StringBuilder();

	        Random random = new Random();

	        for (int i = 0; i < length; i++) {
	            int index = random.nextInt(characters.length());
	            char randomChar = characters.charAt(index);
	            randomString.append(randomChar);
	        }

	        return randomString.toString();
	 }
	

	public List<Object> allTasks() {
		var responseList = new ArrayList<>();

		var tasks = taskService.createTaskQuery().list().stream().toList();

		tasks.stream().map(task -> {
			var responseMap = new HashMap<>();

			Map<String, Object> variables = taskService.getVariables(task.getId());

			responseMap.put("id", task.getId());
			responseMap.put("name", task.getName());
	//	responseMap.put("author", (String) variables.get("author"));
	//		responseMap.put("url", (String) variables.get("url"));

			responseList.add(responseMap);
			return null;
		}).collect(Collectors.toList());

		return responseList;
	}

/*
	public void startBigBoy(String id) {

		boolean update = false;

		String resourceName = "decisionTreeFlow.bpmn";
		String stanceName = "decisionTreeProcess";

		// Verifica se já existe um deployment com o mesmo nome
		List<Deployment> dps = repositoryService.createDeploymentQuery().deploymentName(resourceName).list();

		// Se já existe, exclui o deployment existente
		for (Deployment existingDeployment : dps) {

			if (update && existingDeployment != null) {
				repositoryService.deleteDeployment(existingDeployment.getId(), true); // O segundo parâmetro indica se
																						// as instâncias de processo
																						// relacionadas devem ser
																						// removidas
				System.out.println("deploy de " + resourceName + " está sendo removido para ser atualizado");
			}

		}

		if (update) {
			repositoryService.createDeployment().name(resourceName).addClasspathResource(resourceName).deploy();
			System.out.println("deploy de " + resourceName + " feito");
		}

		ProcessInstance processInstance = null;

		if (id == null) {
			System.out.println("criando uma nova instancia do processo");
			processInstance = runtimeService.startProcessInstanceByKey(stanceName);
		} else {
			System.out.println("carregando a instancia: " + id);
			processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(id).singleResult();
			if (processInstance == null) {
				System.out.println(id + " não encontrado");
				return;
			}
		}

		System.out.println("Processo iniciado com sucesso. ID da instância do processo: " + processInstance.getId());

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).active().list();

		if (!tasks.isEmpty()) {

			for (Task task : tasks) {
				System.out.println("Tarefa atual: " + task.getName());

				Map<String, Object> variables = new HashMap<>();
				Random r = new Random();
				
				StringBuffer b = new StringBuffer();
				
				for (int i = 0; i < 1024 * 1024 * 28; i++) {
					b.append("A");
				}
				
				variables.put("var_e2", b.toString());
				variables.put("var_e",100);

				taskService.complete(task.getId(), variables);
				System.out.println("Tarefa executada com " + variables.toString());
			}
		} else {
			System.out.println("Não há tarefas ativas para a instância do processo.");
		}

	}
	*/
}
