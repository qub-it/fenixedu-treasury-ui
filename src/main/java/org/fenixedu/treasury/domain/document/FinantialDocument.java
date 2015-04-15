package org.fenixedu.treasury.domain.document;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class FinantialDocument extends FinantialDocument_Base {
    
    public FinantialDocument() {
        super();
    }
    
    public String getUiDocumentNumber()
    {
    	return String.format("%s %s/%s", this.getDocumentNumberSeries().getFinantialDocumentType(),this.getDocumentNumberSeries().getSeries().getCode(), this.getDocumentNumber());
    }
    
    public BigDecimal getTotalValue()
    {
    	return BigDecimal.ZERO;
    }

    
    public BigDecimal getTotalNetValue()
    {
    	return BigDecimal.ZERO;
    }

	public Boolean getClosed() {
		return this.getState().equals(FinantialDocumentState.CLOSED);
	}

	public DateTime getWhenCreated() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserChanged() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getUserCreated() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
