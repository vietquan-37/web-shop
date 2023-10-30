export interface AuthenticationResponse{
  userId?:number;
  accessToken?:string;
  refreshToken?:string;
  mfaEnable?:string;
  secretImage?:string;
  role?:string
}
