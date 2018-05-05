package com.chrisrm.idea.actions;

import com.chrisrm.idea.MTThemes;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.intellij.openapi.wm.impl.IdeBackgroundUtil.EDITOR_PROP;

public final class WeebShitManager {

  private final static WeebShitManager instance = new WeebShitManager();
  private static final String WEEB_SHIT_PROPERTY = "WEEB_SHIT_PROPERTY";
  private final AtomicBoolean isOn = new AtomicBoolean(false);
  private Optional<Project> projectRef = Optional.empty();

  private WeebShitManager() {
  }

  public static WeebShitManager getInstance() {
    return instance;
  }

  public void setProjectRef(Project projectRef) {
    this.projectRef = Optional.of(projectRef);
    this.projectRef.ifPresent(project1 -> {
      PropertiesComponent instance = PropertiesComponent.getInstance();
      isOn.getAndSet(instance.getBoolean(WEEB_SHIT_PROPERTY, false));
      if (isOn.get())
        turnOnWeebShit(project1);
    });
  }

  public boolean weebShitOn() {
    return isOn.get();
  }

  public void toggleWeebShit() {
    this.projectRef.ifPresent(project -> {
      boolean weebShitIsOn = isOn.get();
      handleWeebShit(weebShitIsOn, project);
      PropertiesComponent.getInstance()
          .setValue(WEEB_SHIT_PROPERTY, isOn.getAndSet(!weebShitIsOn));
    });
  }

  private void handleWeebShit(boolean weebShitIsOn, Project project) {
    if (weebShitIsOn) {
      removeWeebShit(project);
    } else {
      turnOnWeebShit(project);
    }
  }

  private void turnOnWeebShit(Project project) {
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, "");
  }

  private void removeWeebShit(Project project) {
    PropertiesComponent.getInstance().setValue(EDITOR_PROP, null);
  }

  public void activate(MTThemes monika) {

  }
}
