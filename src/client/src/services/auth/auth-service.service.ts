import { Injectable } from "@angular/core";
import { IAuthService } from "./auth-service.interface";
import { Observable, tap } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
})
export class AuthService implements IAuthService {

    private apiUrl: string = 'http://localhost:8080/api/auth';

    constructor(private httpClient: HttpClient) { }

    login(param: string): Observable<any> {
        return this.httpClient.post(`${this.apiUrl}/login`, param).pipe(tap((res: any) => {
            const token = res.accessToken;
            const userInfo = JSON.stringify(res.user);
            if (token != null) {
                localStorage.setItem('token', token);
                localStorage.setItem('userInfo', userInfo);
            }
        }));
    }
    register(param: any): Observable<any> {
        return this.httpClient.post(`${this.apiUrl}/register`, param);
    }

}