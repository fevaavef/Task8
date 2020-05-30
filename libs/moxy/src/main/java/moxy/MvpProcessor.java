package moxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import moxy.locators.PresenterBinderLocator;
import moxy.presenter.InjectPresenter;
import moxy.presenter.PresenterField;

public class MvpProcessor {

    public static final String PRESENTER_BINDER_SUFFIX = "$$PresentersBinder";

    public static final String PRESENTER_BINDER_INNER_SUFFIX = "Binder";

    public static final String VIEW_STATE_SUFFIX = "$$State";

    public static final String VIEW_STATE_PROVIDER_SUFFIX = "$$ViewStateProvider";

    /**
     * <p>1) Generates tag for the identification of MvpPresenter</p>
     * <p>2) Checks if a presenter with the tag is already exists in the {@link PresenterStore} and returns it</p>
     * <p>3) If the {@link PresenterStore} doesn't contain MvpPresenter with current tag,
     * {@link PresenterField} will create it</p>
     *
     * @param <Delegated> type of delegated
     * @param target object that want injection
     * @param presenterField info about presenter from {@link InjectPresenter}
     * @param delegateTag unique tag generated by {@link moxy.MvpDelegate#generateTag()}
     * @return MvpPresenter instance
     */
    private <Delegated> MvpPresenter<? super Delegated> getMvpPresenter(Delegated target,
        PresenterField<Delegated> presenterField, String delegateTag) {
        Class<? extends MvpPresenter> presenterClass = presenterField.getPresenterClass();
        PresenterStore presenterStore = MvpFacade.getInstance().getPresenterStore();

        String tag = delegateTag + "$" + presenterField.getTag(target);

        //noinspection unchecked
        MvpPresenter<? super Delegated> presenter = presenterStore.get(tag);
        if (presenter != null) {
            return presenter;
        }

        //noinspection unchecked
        presenter = (MvpPresenter<? super Delegated>) presenterField.providePresenter(target);

        if (presenter == null) {
            return null;
        }

        presenter.setTag(tag);
        presenter.setPresenterClass(presenterClass);
        presenterStore.add(tag, presenter);

        return presenter;
    }

    /**
     * <p>Gets presenters {@link java.util.List} annotated with {@link InjectPresenter} for view.</p>
     * <p>See full info about getting presenter instance in {@link #getMvpPresenter}</p>
     *
     * @param delegated class containing presenter
     * @param delegateTag unique tag generated by {@link MvpDelegate#generateTag()}
     * @param <Delegated> type of delegated
     * @param externalPresenterFields additional fields not generated by annotation processor
     * @return presenters list for the specified presenters container
     */
    <Delegated> List<MvpPresenter<? super Delegated>> getMvpPresenters(
            Delegated delegated, String delegateTag, Set<PresenterField<? super Delegated>> externalPresenterFields) {
        @SuppressWarnings("unchecked")
        Class<? super Delegated> aClass = (Class<Delegated>) delegated.getClass();
        PresenterBinder<Delegated> presenterBinder = PresenterBinderLocator.getPresenterBinders(aClass);
        List<PresenterField<? super Delegated>> presenterFields =
                combinePresenterFields(presenterBinder, externalPresenterFields);

        if (presenterFields.isEmpty()) {
            return Collections.emptyList();
        }

        List<MvpPresenter<? super Delegated>> presenters = new ArrayList<>();
        PresentersCounter presentersCounter = MvpFacade.getInstance().getPresentersCounter();

        for (PresenterField<? super Delegated> presenterField : presenterFields) {
            MvpPresenter<? super Delegated> presenter =
                    (MvpPresenter<? super Delegated>) getMvpPresenter(delegated, presenterField, delegateTag);

            if (presenter != null) {
                presentersCounter.injectPresenter(presenter, delegateTag);
                presenters.add(presenter);
                presenterField.bind(delegated, presenter);
            }
        }

        return presenters;
    }

    private <Delegated> List<PresenterField<? super Delegated>> combinePresenterFields(
            PresenterBinder<Delegated> presenterBinder, Set<PresenterField<? super Delegated>> externalFields) {
        ArrayList<PresenterField<? super Delegated>> presenterFields = new ArrayList<>();
        if (presenterBinder != null) {
            presenterFields.addAll(presenterBinder.getPresenterFields());
        }
        presenterFields.addAll(externalFields);
        return presenterFields;
    }
}