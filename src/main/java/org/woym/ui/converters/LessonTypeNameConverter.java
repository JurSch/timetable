package org.woym.ui.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.woym.common.exceptions.DatasetException;
import org.woym.common.messages.GenericErrorMessage;
import org.woym.common.messages.MessageHelper;
import org.woym.common.objects.ActivityType;
import org.woym.common.objects.LessonType;
import org.woym.persistence.DataAccess;

/**
 * <h1>LessonTypeNameConverter</h1>
 * <p>
 * Diese Controller ist dafür zuständig, die in den Listen zur Auswahl von
 * Unterrichtsinhalten dargestellten Objekte richtig darzustellen.
 * 
 * 
 * @author Tim Hansen (tihansen)
 */
@FacesConverter("org.woym.LessonTypeNameConverter")
public class LessonTypeNameConverter implements Converter {

	private DataAccess dataAccess = DataAccess.getInstance();
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent uiComponent,
			String value) throws ConverterException {

		ActivityType lessonType = new LessonType();

		try {
			lessonType = dataAccess.getOneActivityType(value);
		} catch (DatasetException e) {
			FacesMessage msg = MessageHelper.generateMessage(GenericErrorMessage.DATABASE_COMMUNICATION_ERROR, FacesMessage.SEVERITY_ERROR);
			throw new ConverterException(msg);
		}

		return lessonType;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent uiComponent,
			Object value) throws ConverterException {
		if(value == null) {
			return "";
		}
		
		ActivityType lessonType = (ActivityType) value;
		return lessonType.getName();
	}

}
