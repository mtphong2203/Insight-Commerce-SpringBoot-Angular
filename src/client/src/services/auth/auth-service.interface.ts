import { Observable } from "rxjs";

export interface IAuthService {
    login(param: string): Observable<any>;

    register(param: any): Observable<any>;
}