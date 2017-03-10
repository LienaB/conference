export default class {
  constructor(Conference) {
    'ngInject';

    this.conferenceService = Conference;

    this.view = 'upcoming';    // default view
    this.conferences = [];
    this.getConferences();     // get initial conferences collection

    this.popupOpen = false;
  }

  conditionalClass(condition) {
    return (this.view === condition) ? 'tabs-list__anchor_active' : '';
  }

  showView(viewName) {
    this.view = viewName;
    this.getConferences();
  }

  getConferences() {
    switch (this.view) {
      case 'upcoming':
        this.conferences = this.conferenceService.getUpcoming();
        break;
      case 'past':
        this.conferences = this.conferenceService.getPast();
        break;
      default:
        this.conferences = [];
    }
  }

  showPopup(conferenceId) {
    this.conferenceId = conferenceId;
    this.popupOpen = true;
  }

  popupCloseCallback() {
    this.popupOpen = false;
  }

  popupSubmitCallback() {
    this.popupOpen = false;
  }
}
