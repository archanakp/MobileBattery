package android.app.mobilebattery.Pojo;

public class IncentiveListPojo {

    private String id, agent_id, lead_id, incentive_amount, created_at, updated_at, agent_name, lead;
    private String lead_receipt_no;

    public IncentiveListPojo() {
    }

    public String getLead_receipt_no() {
        return lead_receipt_no;
    }

    public void setLead_receipt_no(String lead_receipt_no) {
        this.lead_receipt_no = lead_receipt_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getIncentive_amount() {
        return incentive_amount;
    }

    public void setIncentive_amount(String incentive_amount) {
        this.incentive_amount = incentive_amount;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }
}
