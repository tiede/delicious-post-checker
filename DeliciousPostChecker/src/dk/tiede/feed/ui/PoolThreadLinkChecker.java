/*
 * ThreadedLinkChecker.java
 *
 * Created on 2. april 2006, 17:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dk.tiede.feed.ui;

import dk.tiede.linkchecker.LinkChecker;
import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.MethodDescriptor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jdesktop.swingworker.SwingWorker;

/**
 *
 * @author kbt
 */
public class PoolThreadLinkChecker implements PropertyChangeListener {
    
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private List swingWorkers;
    private PropertyChangeSupport changeSupport;
    private int totalLinks = 0;
    private Long progress = 0L;
    private int noOfDone = 0;
    
    /** Creates a new instance of ThreadedLinkChecker */
    public PoolThreadLinkChecker(int totalLinks) {
        swingWorkers = new ArrayList();
        this.changeSupport = new PropertyChangeSupport(this);
        this.totalLinks = totalLinks;
    }
    
    public<T,V> void submitLinkCheck(SwingWorker<T,V> worker) {
        worker.addPropertyChangeListener(this);
        swingWorkers.add(worker);
        threadPool.execute(worker);
        System.out.println("Started " + swingWorkers.size() + " workers");
    }

    public synchronized void propertyChange(PropertyChangeEvent evt) {
        if ("done".equals(evt.getPropertyName())) {
            noOfDone++;
            Long progressNew = Math.round((noOfDone/(double)totalLinks) * 100.0);
            changeSupport.firePropertyChange("progress", progress, progressNew);
            changeSupport.firePropertyChange("subtaskDone", Integer.valueOf(noOfDone - 1), Integer.valueOf(noOfDone));
            progress = progressNew;
            
            System.out.println("Total links : " + totalLinks + " | NoOfDone : " + noOfDone);    
            if (totalLinks == noOfDone) {
                changeSupport.firePropertyChange("done", false, true);
            }
        }
    }
    
    public void shutdownPool() {
        threadPool.shutdown();
    }
    
    public void shutdownPoolNow() {
        threadPool.shutdownNow();
    }
    
    public boolean isTerminated() {
        return threadPool.isTerminated();
    }
    
    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        this.changeSupport.addPropertyChangeListener(listener);
    }
}
