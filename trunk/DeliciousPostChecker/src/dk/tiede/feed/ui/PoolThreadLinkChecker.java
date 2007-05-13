// Copyright 2006 Kim Bjørn Tiedemann (www.tiede.dk/blog.tiede.dk) 
// Licensed under the Apache License, Version 2.0 (the "License"); you may not 
// use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//   http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
// See the License for the specific language governing permissions and 
// limitations under the License.

/*
 * ThreadedLinkChecker.java
 *
 * Created on 2. april 2006, 17:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dk.tiede.feed.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
    
    /**
     *
     */
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
