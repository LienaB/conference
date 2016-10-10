/* global include_all_modules */
import homeComponent from './home/home';
import loginComponent from './login/login';
import listComponent from './list/list';

export default (app) => {
  include_all_modules([homeComponent, loginComponent, listComponent], app);
};
