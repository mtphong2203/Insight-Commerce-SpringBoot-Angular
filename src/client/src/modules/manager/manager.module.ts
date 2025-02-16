import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { UserListComponent } from './users/user-list.component';
import { RoleListComponent } from './roles/role-list.component';

const routes: Routes = [
  {
    path: 'users',
    component: UserListComponent
  },
  {
    path: 'roles',
    component: RoleListComponent
  },
]

@NgModule({
  declarations: [],
  imports: [
    CommonModule, RouterModule.forChild(routes)
  ]
})
export class ManagerModule { }
