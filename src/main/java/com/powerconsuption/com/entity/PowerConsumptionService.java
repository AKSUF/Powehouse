package com.powerconsuption.com.entity;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Service;
@Service
public class PowerConsumptionService {
	   private static final String JVM_RUNTIME_BEAN_NAME = ManagementFactory.RUNTIME_MXBEAN_NAME;
	    private static final String HEAP_MEMORY_USAGE_ATTRIBUTE_NAME = "HeapMemoryUsage";
	    private static final String PROCESS_CPU_LOAD_ATTRIBUTE_NAME = "ProcessCpuLoad";

	    public double getPowerConsumption(int pid) throws Exception {
	        double powerConsumption = 0.0;

	        // Connect to the local JVM's JMX server
	        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
	        JMXConnector jmxc = JMXConnectorFactory.connect(url);
	        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

	        // Get the process name for the given PID
	        String processName = getProcessName(mbsc, pid);

	        // Get the heap memory usage for the process
	        ObjectName heapMemoryObjectName = new ObjectName("java.lang:type=MemoryPool,name=PS Eden Space");
	        long usedHeapMemory = (long) mbsc.getAttribute(heapMemoryObjectName, "Usage").getClass().getMethod("getUsed")
	                .invoke(mbsc.getAttribute(heapMemoryObjectName, "Usage"));

	        // Get the process CPU load
	        double processCpuLoad = getProcessCpuLoad(mbsc, processName);

	        // Calculate the power consumption
	        powerConsumption = calculateWatts(usedHeapMemory, processCpuLoad);

	        // Close the JMX connection
	        jmxc.close();

	        return powerConsumption;
	    }

	    private String getProcessName(MBeanServerConnection mbsc, int pid) throws IOException {
	        OperatingSystemMXBean osMBean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
	                ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
	        String processName = osMBean.getName();
	        List<RuntimeMXBean> runtimeMXBeans = ManagementFactory.getPlatformMXBeans(RuntimeMXBean.class);
	        for (RuntimeMXBean runtimeMXBean : runtimeMXBeans) {
	            if (runtimeMXBean.getName().split("@")[0].equals(String.valueOf(pid))) {
	                processName = runtimeMXBean.getName().split("@")[1];
	                break;
	            }
	        }
	        return processName;
	    }

	    private double getProcessCpuLoad(MBeanServerConnection mbsc, String processName) throws Exception {
	        ObjectName processCpuLoadObjectName = new ObjectName("java.lang:type=OperatingSystem,name=" + processName);
	        double processCpuLoad = (double) mbsc.getAttribute(processCpuLoadObjectName, PROCESS_CPU_LOAD_ATTRIBUTE_NAME);
	        return processCpuLoad;
	    }

	    private double calculateWatts(long usedHeapMemory, double processCpuLoad) {
	        // This method calculates the power consumption in watts for a given used heap memory (in bytes)
	        // and process CPU load percentage.
	        // You can replace this with your own power consumption model or algorithm.

	        double watts = usedHeapMemory / 1048576.0 * 0.003; // Base power consumption based on used heap memory
	        watts += (processCpuLoad / 100.0) * 5.0; // Additional power consumption based on process CPU load

	        return watts;
	    }

}
