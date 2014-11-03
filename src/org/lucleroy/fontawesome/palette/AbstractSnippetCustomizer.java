package org.lucleroy.fontawesome.palette;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 *
 * @author PROPRIETAIRE
 */
public abstract class AbstractSnippetCustomizer extends JPanel {

    private boolean dialogOK;
    private DialogDescriptor descriptor;
    private Dialog dialog;
    private AbstractSnippet snippet;

    public AbstractSnippetCustomizer(AbstractSnippet snippet) {
        this.snippet = snippet;
    }

    public AbstractSnippet getSnippet() {
        return snippet;
    }

    public boolean showDialog() {
        dialogOK = false;
        descriptor = new DialogDescriptor(this, getTitle(), true,
                DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (descriptor.getValue().equals(DialogDescriptor.OK_OPTION)) {
                            evaluateInput();
                            dialogOK = true;
                        }
                        dialog.dispose();
                    }
                });
        dialog = DialogDisplayer.getDefault().createDialog(descriptor);
        dialog.setVisible(true);
        return dialogOK;
    }

    protected abstract void evaluateInput(); 
    
    protected abstract String getTitle();
}
