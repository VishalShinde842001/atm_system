
import { Routes } from '@angular/router';
import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { AtmComponent } from './atm/atm.component';
import { NotificationsComponent } from './notifications/notifications.component';

export const routes: Routes = [{
    path:"register",component:RegisterComponent
},{
    path:"login",component:LoginComponent
},{
    path:"atmServices",component:AtmComponent
},{
    path:"notifications",component:NotificationsComponent
}

]
