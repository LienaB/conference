export default class {
  constructor(Menus, Talks) {
    'ngInject';

    this.talksService = Talks;
    this.selectService = Menus;
    this.talkForm = {};

    this.talk = Talks.get(this.id);

    this.commentRequired = false;
  }

  approve() {
    this.talksService.approve(this.id, this.talk.comment,
      () => { this.close(); });   // success callback
  }

  reject() {
    if (!this.talk.comment) { // required
      this.commentRequired = true;
      return;
    }
    this.talksService.reject(this.id, this.talk.comment,
      () => { this.close(); });   // success callback
  }

  progress() {
    if (!this.talk.comment) { // required
      this.commentRequired = true;
      return;
    }
    this.talksService.progress(this.id, this.talk.comment,
      () => { this.close(); });   // success callback
  }

  close() {
    this.onHideReviewPopup();
  }

  resetCommentRequired() {
    this.commentRequired = false;
  }
}