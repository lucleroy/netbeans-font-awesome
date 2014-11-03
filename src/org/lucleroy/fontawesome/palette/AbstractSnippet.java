package org.lucleroy.fontawesome.palette;

import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.editor.indent.api.Indent;
import org.openide.text.ActiveEditorDrop;

public abstract class AbstractSnippet implements ActiveEditorDrop {

    public AbstractSnippetCustomizer getCustomizerInstance() {
        return null;
    }

    protected abstract String createBody();

    @Override
    public boolean handleTransfer(JTextComponent targetComponent) {
        boolean accept = tranferAccepted();
        if (accept) {
            String body = createBody();
            try {
                insert(body, targetComponent);
            } catch (BadLocationException ble) {
                accept = false;
            }
        }
        return accept;
    }

    private boolean tranferAccepted() {
        AbstractSnippetCustomizer customizer = getCustomizerInstance();
        if (customizer == null) {
            return true;
        }
        return customizer.showDialog();
    }

    private void insert(final String body, final JTextComponent targetComponent) throws BadLocationException {
        final BaseDocument doc = (BaseDocument) targetComponent.getDocument();

        class AtomicChange implements Runnable {

            @Override
            public void run() {
                Document value = targetComponent.getDocument();
                if (value == null) {
                    return;
                }
                try {
                    insert(body, targetComponent, doc);
                } catch (BadLocationException e) {
                }
            }
        }

        doc.runAtomicAsUser(new AtomicChange());
    }

    private void insert(String body, JTextComponent targetComponent, BaseDocument doc) throws BadLocationException {
        Caret caret = targetComponent.getCaret();
        int p0 = Math.min(caret.getDot(), caret.getMark());
        int p1 = Math.max(caret.getDot(), caret.getMark());
        doc.remove(p0, p1 - p0);

        int start = caret.getDot();
        doc.insertString(start, body, null);

        Indent indent = Indent.get(doc);
        indent.lock();
        indent.reindent(start, caret.getDot());
        indent.unlock();
        
        targetComponent.transferFocus();
    }

}
