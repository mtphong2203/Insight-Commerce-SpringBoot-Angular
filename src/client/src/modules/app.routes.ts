import { Routes } from '@angular/router';
import { AuthLayoutComponent } from './shared/layouts/auth-layout/auth-layout.component';
import { ManagerLayoutComponent } from './shared/layouts/manager-layout/manager-layout.component';
import { CustomerLayoutComponent } from './shared/layouts/customer-layout/customer-layout.component';

export const routes: Routes = [
    {
        path: 'auth',
        component: AuthLayoutComponent,
        loadChildren: () => import('./auth/auth.module').then(m => m.AuthModule)
    },
    {
        path: 'management',
        component: ManagerLayoutComponent,
        loadChildren: () => import('./manager/manager.module').then(m => m.ManagerModule)
    },
    {
        path: '',
        component: CustomerLayoutComponent,
        loadChildren: () => import("./customer/customer.module").then(m => m.CustomerModule)
    },
    {
        path: '**',
        redirectTo: '',
        pathMatch: 'full'
    }

];
