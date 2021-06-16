package ca.ualberta.cs.smr.core.matrix.dispatcher;

import ca.ualberta.cs.smr.core.matrix.receivers.Receiver;

public class InlineMethodDispatcher extends RefactoringDispatcher {
    @Override
    public void dispatch(Receiver r) {
        r.receive(this);
    }
}