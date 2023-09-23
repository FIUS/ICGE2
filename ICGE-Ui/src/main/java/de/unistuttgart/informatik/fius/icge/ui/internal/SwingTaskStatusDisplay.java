/*
 * This source file is part of the FIUS ICGE project.
 * For more information see github.com/FIUS/ICGE2
 * 
 * Copyright (c) 2019 the ICGE project authors.
 * 
 * This software is available under the MIT license.
 * SPDX-License-Identifier:    MIT
 */
package de.unistuttgart.informatik.fius.icge.ui.internal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import de.unistuttgart.informatik.fius.icge.ui.SimulationProxy;
import de.unistuttgart.informatik.fius.icge.ui.TaskInformation;
import de.unistuttgart.informatik.fius.icge.ui.TaskStatusDisplay;


/**
 * An implementation of {@link TaskStatusDisplay} using java swing.
 *
 * @author Fabian Bühler
 * @version 1.0
 */
public class SwingTaskStatusDisplay extends JPanel implements TaskStatusDisplay {
    
    private static final long serialVersionUID = -2711911902591163118L;
    
    private final JTextPane textPane;
    
    private final Style textStyle;
    private final Style taskTitle;
    private final Style taskSuccess;
    private final Style taskFail;
    
    private SimulationProxy simulationProxy;
    
    /**
     * Default constructor.
     * <p>
     * This should only be called from the swing ui thread
     * 
     * @param fontScale
     *     the scaling value for the fontSize
     */
    public SwingTaskStatusDisplay(final double fontScale) {
        super(new BorderLayout());
        
        // setup text pane
        this.textPane = new JTextPane(new DefaultStyledDocument());
        this.textPane.setEditable(false);
        ((DefaultCaret) this.textPane.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        
        // setup text styles
        final int fontSize = (int) Math.floor(12 * fontScale);
        this.textStyle = this.textPane.addStyle("Text", null);
        StyleConstants.setFontFamily(this.textStyle, "serif");
        StyleConstants.setFontSize(this.textStyle, fontSize);
        
        this.taskTitle = this.textPane.addStyle("TaskTitle", this.textStyle);
        StyleConstants.setBold(this.taskTitle, true);
        
        this.taskSuccess = this.textPane.addStyle("TaskSuccess", this.taskTitle);
        this.taskFail = this.textPane.addStyle("TaskFail", this.taskTitle);
        
        StyleConstants.setForeground(this.taskSuccess, Color.GREEN);
        StyleConstants.setForeground(this.taskFail, Color.RED);
        
        // setup refresh button
        final JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(ae -> {
            if (this.simulationProxy != null) {
                this.simulationProxy.refreshTaskInformation();
            }
        });
        
        // pack component
        this.add(new JScrollPane(this.textPane), BorderLayout.CENTER);
        this.add(refreshButton, BorderLayout.LINE_END);
    }
    
    /**
     * Set the simulation proxy. TODO better doc
     *
     * @param simulationProxy
     *     The simulation proxy this SwingPlayfieldDrawer should subscribe to
     */
    public void setSimulationProxy(final SimulationProxy simulationProxy) {
        if (this.simulationProxy != null) throw new IllegalStateException("SimulationProxy is already set and cannot be overwritten!");
        
        this.simulationProxy = simulationProxy;
    }
    
    @Override
    public void setTaskInformation(final TaskInformation task) {
        // invoke later to break out of event thread of the refresh button press handler
        SwingUtilities.invokeLater(() -> {
            this.textPane.setText(""); // reset document
            final StyledDocument document = this.textPane.getStyledDocument();
            if (task != null) {
                this.appendTaskInformation(task, document, 0);
            } else {
                this.appendText(document, "No task set!\n", this.taskTitle);
                this.appendText(document, "You can set a task verifier in the SimulationBuilder.", this.textStyle);
            }
        });
    }
    
    /**
     * Appends the task information of the task and all subtasks to the styled document.
     * 
     * @param task
     *     the task information to add
     * @param document
     *     the document to append the information to
     * @param depth
     *     the current task depth (starts with 0, may be used to indent subtasks later)
     */
    private void appendTaskInformation(final TaskInformation task, final StyledDocument document, final int depth) {
        
        // append title
        String title = task.getTaskTitle();
        if (title == null || title.length() == 0) {
            title = "Unnamed Task";
        }
        this.appendText(document, indentText(title, depth), this.taskTitle);
        
        // append task status
        switch (task.getTaskStatus()) {
            case SUCCESSFUL:
                this.appendText(document, " (success)\n", this.taskSuccess);
                break;
            case FAILED:
                this.appendText(document, " (failed)\n", this.taskFail);
                break;
            case UNDECIDED: // don't add any text for undecided status (same as default)
            default:
                this.appendText(document, " (pending)\n", this.taskTitle);
        }
        
        // append description
        final String description = task.getTaskDescription();
        if (description != null && description.length() > 0) {
            this.appendText(document, indentText(description + '\n', depth + 2), this.textStyle);
        }
        
        // handle subtasks
        final List<TaskInformation> childTasks = task.getChildTasks();
        if (childTasks != null) {
            for (final TaskInformation subTask : childTasks) {
                this.appendText(document, "\n", this.textStyle);
                this.appendTaskInformation(subTask, document, depth + 1);
            }
        }
    }
    
    /**
     * Appends text to a styled document while silently dismissing {@link BadLocationException}.
     *
     * @param document
     *     the document to append to
     * @param text
     *     the text to append
     * @param style
     *     the style of the text to append
     */
    private void appendText(final StyledDocument document, final String text, final Style style) {
        try {
            document.insertString(document.getLength(), text, style);
        } catch (final BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * This function appends indentation spaces to a string
     *
     * @param text
     *     The string to indent, which may contain newline characters
     * @param depth
     *     The depth of the indention
     * @return Returns the new indented string.
     */
    private static String indentText(final String text, final int depth) {
        if (depth == 0) return text;
        
        StringBuilder returnString = new StringBuilder();
        String indention = "\u2003".repeat(depth);
        
        for (String s : text.split("\n")) {
            returnString.append(indention);
            returnString.append(s);
            returnString.append('\n');
        }
        
        // Delete the final new line character if needed to prevent additional unwanted newlines in the result string
        if (!text.endsWith("\n")) {
            returnString.deleteCharAt(returnString.length() - 1);
        }
        
        return returnString.toString();
    }
}
