import { InjectionToken } from "@angular/core";
import { IAuthService } from "../services/auth/auth-service.interface";

export const AUTH_SERVICE = new InjectionToken<IAuthService>('AUTH_SERVICE');