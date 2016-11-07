package seedu.todo.model;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Logger;

import seedu.todo.commons.core.GuiSettings;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.exceptions.DataConversionException;
import seedu.todo.storage.FixedStorage;
import seedu.todo.storage.UserPrefsStorage;

//@@author A0139021U reused
/**
 * Represents User's preferences.
 */
public class UserPrefs {

    public GuiSettings guiSettings;

    private static final Logger logger = LogsCenter.getLogger(UserPrefs.class);

    private FixedStorage<UserPrefs> storage;

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public UserPrefs(String filePath) {
        this.storage = new UserPrefsStorage(filePath);
        try {
            setUserPrefs(storage.read(), false);
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file is not in the correct format. Using default user prefs.");
            setDefaultUserPrefs();
        } catch (NoSuchElementException e) {
            logger.warning("UserPrefs file does not exist. Using default user prefs.");
            setDefaultUserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Using default user prefs.");
            setDefaultUserPrefs();
        }
    }
    
    private void setDefaultUserPrefs() {
        this.setGuiSettings(500, 500, 0, 0);
    }

    /**
     * We have a private version of userPrefs because we also need to set user prefs
     * during initialization, but we don't want the list to be save during init
     * (where we presumably got the data from)
     */
    private void setUserPrefs(UserPrefs userPref, boolean persistToStorage) {
        this.guiSettings = userPref.getGuiSettings();

        if (persistToStorage) {
            this.save();
        }
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
        this.save();
    }

    /**
     * Saves user preferences to storage and produces silent error 
     * if there are any, as width / height of window is not critical to user.
     */
    public void save() {
        try {
            storage.save(this);
        } catch (IOException e) {
            logger.severe("Data IO error - " + e.getClass().getSimpleName() + " | " + e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { // this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings);
    }

    @Override
    public String toString() {
        return guiSettings.toString();
    }

}
